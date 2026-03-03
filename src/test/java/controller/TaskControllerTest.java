package controller;

import com.myapp.controller.TaskController;
import com.myapp.exception.TaskNotFoundException;
import com.myapp.model.Priority;
import com.myapp.model.Status;
import com.myapp.model.Task;
import com.myapp.service.TaskService;
import org.springframework.http.MediaType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskController.class)
@ContextConfiguration(classes = com.myapp.Application.class)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TaskService taskService;

    @Test
    void shouldReturnAllTasks() throws Exception {
        Task task = new Task("Task 1", "Description", Status.TODO, Priority.HIGH, LocalDate.of(2026, 1, 15));
        setTaskId(task, 1);
        when(taskService.getAllTasks()).thenReturn(List.of(task));

        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].title").value("Task 1"))
                .andExpect(jsonPath("$[0].priority").value("HIGH"));
    }

    @Test
    void shouldReturnTaskById() throws Exception {
        Task task = new Task("Task 1", "Description", Status.IN_PROGRESS, Priority.MEDIUM, LocalDate.of(2026, 2, 1));
        setTaskId(task, 1);
        when(taskService.getTaskById(1)).thenReturn(task);

        mockMvc.perform(get("/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Task 1"))
                .andExpect(jsonPath("$.status").value("IN_PROGRESS"));
    }

    @Test
    void shouldReturn404WhenTaskByIdNotFound() throws Exception {
        when(taskService.getTaskById(999)).thenThrow(new TaskNotFoundException(999));

        mockMvc.perform(get("/tasks/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("NOT_FOUND"));
    }

    @Test
    void shouldCreateTask() throws Exception {
        Task created = new Task("New Task", "Description", Status.TODO, Priority.LOW, LocalDate.of(2026, 3, 10));
        setTaskId(created, 10);

        when(taskService.createTask(
                eq("New Task"),
                eq("Description"),
                eq(Status.TODO),
                eq(Priority.LOW),
                eq(LocalDate.of(2026, 3, 10))
        )).thenReturn(created);

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "title": "New Task",
                                  "description": "Description",
                                  "status": "TODO",
                                  "priority": "LOW",
                                  "dueDate": "2026-03-10"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("New Task"))
                .andExpect(jsonPath("$.priority").value("LOW"));
    }

    @Test
    void shouldUpdateTaskWithPut() throws Exception {
        Task updatedTask = new Task("Task", "Updated description", Status.DONE, Priority.HIGH, LocalDate.of(2026, 5, 1));
        setTaskId(updatedTask, 1);
        when(taskService.updateTask(eq(1), any())).thenReturn(updatedTask);

        mockMvc.perform(put("/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "description": "Updated description",
                                  "status": "DONE",
                                  "priority": "HIGH",
                                  "dueDate": "2026-05-01"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("DONE"))
                .andExpect(jsonPath("$.description").value("Updated description"));
    }

    @Test
    void shouldUpdateTaskWithPatch() throws Exception {
        Task patchedTask = new Task("Task", "Partially updated", Status.IN_PROGRESS, Priority.MEDIUM, LocalDate.of(2026, 6, 1));
        setTaskId(patchedTask, 1);
        when(taskService.updateTask(eq(1), any())).thenReturn(patchedTask);

        mockMvc.perform(patch("/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "description": "Partially updated",
                                  "status": "IN_PROGRESS"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("Partially updated"))
                .andExpect(jsonPath("$.status").value("IN_PROGRESS"));
    }

    @Test
    void shouldDeleteTask() throws Exception {
        mockMvc.perform(delete("/tasks/1"))
                .andExpect(status().isNoContent());

        verify(taskService).deleteTask(1);
    }

    @Test
    void shouldReturn404WhenDeletingUnknownTask() throws Exception {
        doThrow(new TaskNotFoundException(1)).when(taskService).deleteTask(1);

        mockMvc.perform(delete("/tasks/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("NOT_FOUND"));
    }

    @Test
    void shouldDeleteAllTasks() throws Exception {
        mockMvc.perform(delete("/tasks"))
                .andExpect(status().isNoContent());

        verify(taskService).deleteAllTasks();
    }

    private void setTaskId(Task task, int id) {
        try {
            Field field = Task.class.getDeclaredField("id");
            field.setAccessible(true);
            field.set(task, id);
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Impossible de définir l'id de test", e);
        }
    }
}