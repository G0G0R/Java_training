package controller;

import com.myapp.controller.TaskController;
import com.myapp.model.Priority;
import com.myapp.model.Status;
import com.myapp.model.Task;
import com.myapp.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.http.MediaType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskController.class)
@ContextConfiguration(classes = com.myapp.Application.class)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        taskService.deleteAllTasks(); // vide le repository avant chaque test
    }

    @Test
    void shouldReturnAllTasks() throws Exception {
        Task task = new Task("Task 1","Description", Status.TODO, Priority.HIGH, LocalDate.now());

        when(taskService.getTasks(null, null)).thenReturn(List.of(task));

        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].title").value("Task 1"));
    }

    @Test
    void shouldFilterTasksByStatusAndPriority() throws Exception {
        when(taskService.getTasks(Status.TODO, Priority.HIGH)).thenReturn(List.of());

        mockMvc.perform(get("/tasks").param("status", "TODO").param("priority", "HIGH"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(0));
        }

    // ---------------------------------------
    // POST /tasks
    // ---------------------------------------
    @Test
    void shouldCreateTask() throws Exception {
        Task task = new Task(
                "New Task",
                "Description",
                Status.TODO,
                Priority.LOW,
                LocalDate.now().plusDays(10)
        );

        // Mock pour matcher la requête
        when(taskService.createTask(
                eq("New Task"),
                eq("Description"),
                any(),          // status
                eq(Priority.LOW),
                any()           // dueDate
        )).thenReturn(task);

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
            {
              "title": "New Task",
              "description": "Description",
              "priority": "LOW"
            }
            """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("New Task"))
                .andExpect(jsonPath("$.priority").value("LOW"));
    }

    // ---------------------------------------
    // PUT /tasks/{id} (update)
    // ---------------------------------------
    @Test
    void shouldUpdateTask() throws Exception {
        Task updatedTask = new Task(
                "Task",
                "Updated description",
                Status.DONE,
                Priority.HIGH,
                LocalDate.now().plusDays(3)
        );

        when(taskService.updateTask(eq(1), any())).thenReturn(updatedTask);

        String json = """
                {
                  "description": "Updated description",
                  "status": "DONE",
                  "priority": "HIGH"
                }
                """;

        mockMvc.perform(put("/tasks/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("DONE"))
                .andExpect(jsonPath("$.description").value("Updated description"));
    }

    /**
     * ---------------------------------------
     * DELETE /tasks/{id}
     * ---------------------------------------
     */
    @Test
    void shouldDeleteTask() throws Exception {
        mockMvc.perform(delete("/tasks/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldReturn404WhenDeletingUnknownTask() throws Exception {
        mockMvc.perform(delete("/tasks/1"))
                .andExpect(status().isNoContent());
    }

    // ---------------------------------------
    // DELETE /tasks
    // ---------------------------------------
    @Test
    void shouldDeleteAllTasks() throws Exception {
        mockMvc.perform(delete("/tasks"))
                .andExpect(status().isNoContent());
    }
}