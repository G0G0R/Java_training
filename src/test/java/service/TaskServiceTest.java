package service;

import com.myapp.dto.UpdateTaskRequest;
import com.myapp.exception.TaskNotFoundException;
import com.myapp.model.Priority;
import com.myapp.model.Status;
import com.myapp.model.Task;
import com.myapp.repository.TaskRepository;
import com.myapp.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    private Task existingTask;

    @BeforeEach
    void setUp() {
        existingTask = new Task("Réviser Java", "Chapitre JPA", Status.TODO, Priority.MEDIUM, LocalDate.of(2026, 1, 10));
        setTaskId(existingTask, 1);
    }

    @Test
    void createTask_shouldPersistAndReturnTask() {
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> {
            Task saved = invocation.getArgument(0);
            setTaskId(saved, 10);
            return saved;
        });

        Task created = taskService.createTask("Nouvelle tâche", "Description", Status.TODO, Priority.HIGH, LocalDate.of(2026, 5, 2));

        assertNotNull(created);
        assertEquals(10, created.getId());
        assertEquals("Nouvelle tâche", created.getTitle());
        assertEquals("Description", created.getDescription());
        assertEquals(Status.TODO, created.getStatus());
        assertEquals(Priority.HIGH, created.getPriority());
        assertEquals(LocalDate.of(2026, 5, 2), created.getDueDate());
        verify(taskRepository).save(any(Task.class));
    }

    @Test
    void getAllTasks_shouldReturnRepositoryValues() {
        Task secondTask = new Task("Task 2", "Desc 2", Status.DONE, Priority.LOW, LocalDate.of(2026, 2, 1));
        when(taskRepository.findAll()).thenReturn(List.of(existingTask, secondTask));

        List<Task> tasks = taskService.getAllTasks();

        assertEquals(2, tasks.size());
        assertEquals("Réviser Java", tasks.get(0).getTitle());
        assertEquals("Task 2", tasks.get(1).getTitle());
    }

    @Test
    void getTaskById_existingId_shouldReturnTask() {
        when(taskRepository.findById(1)).thenReturn(Optional.of(existingTask));

        Task found = taskService.getTaskById(1);

        assertEquals(1, found.getId());
        assertEquals("Chapitre JPA", found.getDescription());
    }

    @Test
    void getTaskById_unknownId_shouldThrowTaskNotFound() {
        when(taskRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> taskService.getTaskById(999));
    }

    @Test
    void updateTask_shouldPatchOnlyProvidedFieldsAndSave() throws Exception {
        UpdateTaskRequest request = buildRequest("Description mise à jour", Status.IN_PROGRESS, null, LocalDate.of(2026, 12, 31));

        when(taskRepository.findById(1)).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Task updated = taskService.updateTask(1, request);

        assertEquals("Description mise à jour", updated.getDescription());
        assertEquals(Status.IN_PROGRESS, updated.getStatus());
        assertEquals(Priority.MEDIUM, updated.getPriority());
        assertEquals(LocalDate.of(2026, 12, 31), updated.getDueDate());
        verify(taskRepository).save(existingTask);
    }

    @Test
    void updateTask_withUnknownId_shouldThrowTaskNotFound() throws Exception {
        UpdateTaskRequest request = buildRequest("Desc", Status.DONE, Priority.HIGH, LocalDate.of(2026, 12, 1));
        when(taskRepository.findById(9999)).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> taskService.updateTask(9999, request));
        verify(taskRepository, never()).save(any(Task.class));
    }

    @Test
    void deleteTask_existingId_shouldDelete() {
        when(taskRepository.existsById(1)).thenReturn(true);

        taskService.deleteTask(1);

        verify(taskRepository).deleteById(1);
    }

    @Test
    void deleteTask_unknownId_shouldThrowTaskNotFound() {
        when(taskRepository.existsById(404)).thenReturn(false);

        assertThrows(TaskNotFoundException.class, () -> taskService.deleteTask(404));
        verify(taskRepository, never()).deleteById(404);
    }

    @Test
    void deleteAllTasks_shouldDelegateToRepository() {
        taskService.deleteAllTasks();

        verify(taskRepository).deleteAll();
        assertTrue(true);
    }

    private UpdateTaskRequest buildRequest(String description, Status status, Priority priority, LocalDate dueDate) throws Exception {
        UpdateTaskRequest request = new UpdateTaskRequest();
        setField(request, "description", description);
        setField(request, "status", status);
        setField(request, "priority", priority);
        setField(request, "dueDate", dueDate);
        return request;
    }

    private void setField(UpdateTaskRequest request, String fieldName, Object value) throws Exception {
        Field field = UpdateTaskRequest.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(request, value);
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