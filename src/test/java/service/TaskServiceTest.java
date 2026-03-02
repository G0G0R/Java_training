package service;

import com.myapp.dto.UpdateTaskRequest;
import com.myapp.exception.TaskNotFoundException;
import com.myapp.model.Priority;
import com.myapp.model.Status;
import com.myapp.model.Task;
import com.myapp.repository.InMemoryTaskRepository;
import com.myapp.service.TaskService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TaskServiceTest {

    private TaskService taskService;

    @BeforeEach
    void setUp() {
        taskService = new TaskService(new InMemoryTaskRepository());
    }

    @Test
    void createTask_withTitleOnly_shouldSetDefaultDescription() {
        Task task = taskService.createTask("Réviser Java");

        Assertions.assertNotNull(task);
        assertEquals("Réviser Java", task.getTitle());
        assertEquals("", task.getDescription());
        assertEquals(Status.TODO, task.getStatus());
        assertEquals(Priority.MEDIUM, task.getPriority());
        assertEquals(LocalDate.MAX, task.getDueDate());
    }

    @Test
    void createTask_withDescription_shouldStoreDescription() {
        Task task = taskService.createTask(
                "Faire les tests",
                "Écrire les tests unitaires JUnit",
                Status.IN_PROGRESS,
                Priority.HIGH,
                LocalDate.of(2026, 2, 1)
        );

        assertEquals("Faire les tests", task.getTitle());
        assertEquals("Écrire les tests unitaires JUnit", task.getDescription());
        assertEquals(Status.IN_PROGRESS, task.getStatus());
        assertEquals(Priority.HIGH, task.getPriority());
        assertEquals(LocalDate.of(2026, 2, 1), task.getDueDate());
    }

    @Test
    void getTaskById_existingId_shouldReturnTaskWithDescription() {
        Task task = taskService.createTask(
                "Tâche test",
                "Description test",
                Status.TODO,
                Priority.LOW,
                LocalDate.MAX
        );

        Task found = taskService.getTaskById(task.getId());

        Assertions.assertNotNull(found);
        assertEquals(task.getId(), found.getId());
        assertEquals("Description test", found.getDescription());
    }

    @Test
    void getAllTasks_shouldReturnAllTasksWithDescriptions() {
        taskService.createTask("Tâche 1");
        taskService.createTask(
                "Tâche 2",
                "Deuxième tâche",
                Status.TODO,
                Priority.MEDIUM,
                LocalDate.MAX
        );

        List<Task> tasks = taskService.getAllTasks();

        assertEquals(2, tasks.size());
        assertEquals("", tasks.get(0).getDescription());
        assertEquals("Deuxième tâche", tasks.get(1).getDescription());
    }

    @Test
    void deleteTask_existingId_shouldRemoveTask() {
        Task task = taskService.createTask(
                "À supprimer",
                "Cette tâche sera supprimée",
                Status.TODO,
                Priority.LOW,
                LocalDate.MAX
        );

        taskService.deleteTask(task.getId());
        int id = task.getId();
        assertThrows(TaskNotFoundException.class, () -> taskService.getTaskById(id));
        Assertions.assertTrue(taskService.getAllTasks().isEmpty());
    }

    @Test
    void deleteAllTasks_shouldRemoveAllTasksRegardlessOfDescription() {
        taskService.createTask("Tâche 1");
        taskService.createTask(
                "Tâche 2",
                "Avec description",
                Status.DONE,
                Priority.HIGH,
                LocalDate.MAX
        );

        taskService.deleteAllTasks();

        Assertions.assertTrue(taskService.getAllTasks().isEmpty());
    }

    @Test
    void updateTaskDescription_shouldUpdateDescription() {
        Task task = taskService.createTask("Titre");

        taskService.updateDescription(task.getId(), "Nouvelle description");

        Task updated = taskService.getTaskById(task.getId());
        assertEquals("Nouvelle description", updated.getDescription());
    }

    @Test
    void updateTaskDescription_withNull_shouldSetEmptyString() {
        Task task = taskService.createTask("Titre", "Desc", Status.TODO, Priority.MEDIUM, LocalDate.MAX);

        taskService.updateDescription(task.getId(), null);

        assertEquals("", task.getDescription());
    }

    @Test
    void updateTaskStatus_shouldUpdateStatus() {
        Task task = taskService.createTask("Titre");

        taskService.updateStatus(task.getId(), Status.DONE);

        assertEquals(Status.DONE, task.getStatus());
    }

    @Test
    void updateTaskPriority_shouldUpdatePriority() {
        Task task = taskService.createTask("Titre");

        taskService.updatePriority(task.getId(), Priority.HIGH);

        assertEquals(Priority.HIGH, task.getPriority());
    }

    @Test
    void updateTaskDueDate_shouldUpdateDueDate() {
        Task task = taskService.createTask("Titre");
        LocalDate newDate = LocalDate.of(2026, 6, 1);

        taskService.updateDueDate(task.getId(), newDate);

        assertEquals(newDate, task.getDueDate());
    }

    @Test
    void updateTask_withUnknownId_shouldThrowException() {
        assertThrows(
                TaskNotFoundException.class,
                () -> taskService.updateStatus(999, Status.DONE)
        );
    }

    @Test
    void updateTask_shouldReplaceAllFields() throws Exception {
        Task task = taskService.createTask("Titre", "Ancienne desc", Status.TODO, Priority.LOW, LocalDate.of(2026, 1, 1));
        UpdateTaskRequest request = buildRequest("Nouvelle desc", Status.DONE, Priority.HIGH, LocalDate.of(2026, 12, 1));

        Task updated = taskService.updateTask(task.getId(), request);

        assertEquals("Nouvelle desc", updated.getDescription());
        assertEquals(Status.DONE, updated.getStatus());
        assertEquals(Priority.HIGH, updated.getPriority());
        assertEquals(LocalDate.of(2026, 12, 1), updated.getDueDate());
    }

    @Test
    void updateTask_withUnknownId_shouldThrowTaskNotFound() throws Exception {
        UpdateTaskRequest request = buildRequest("Desc", Status.DONE, Priority.HIGH, LocalDate.of(2026, 12, 1));

        assertThrows(TaskNotFoundException.class, () -> taskService.updateTask(9999, request));
    }

    @Test
    void patchTask_shouldOnlyUpdateNonNullFields() throws Exception {
        Task task = taskService.createTask("Titre", "Ancienne desc", Status.TODO, Priority.LOW, LocalDate.of(2026, 1, 1));
        UpdateTaskRequest request = buildRequest(null, Status.IN_PROGRESS, null, LocalDate.of(2026, 6, 6));

        Task patched = taskService.patchTask(task.getId(), request);

        assertEquals("Ancienne desc", patched.getDescription());
        assertEquals(Status.IN_PROGRESS, patched.getStatus());
        assertEquals(Priority.LOW, patched.getPriority());
        assertEquals(LocalDate.of(2026, 6, 6), patched.getDueDate());
    }

    @Test
    void patchTask_withUnknownId_shouldThrowTaskNotFound() throws Exception {
        UpdateTaskRequest request = buildRequest("Desc", Status.DONE, Priority.HIGH, LocalDate.of(2026, 12, 1));

        assertThrows(TaskNotFoundException.class, () -> taskService.patchTask(9999, request));
    }

    @Test
    void getTasks_shouldFilterByStatusOnly() {
        taskService.createTask("Task 1", "desc", Status.TODO, Priority.LOW, LocalDate.MAX);
        taskService.createTask("Task 2", "desc", Status.DONE, Priority.LOW, LocalDate.MAX);

        List<Task> filtered = taskService.getTasks(Status.DONE, null);

        assertEquals(1, filtered.size());
        assertEquals(Status.DONE, filtered.getFirst().getStatus());
    }

    @Test
    void getTasks_shouldFilterByPriorityOnly() {
        taskService.createTask("Task 1", "desc", Status.TODO, Priority.LOW, LocalDate.MAX);
        taskService.createTask("Task 2", "desc", Status.TODO, Priority.HIGH, LocalDate.MAX);

        List<Task> filtered = taskService.getTasks(null, Priority.HIGH);

        assertEquals(1, filtered.size());
        assertEquals(Priority.HIGH, filtered.getFirst().getPriority());
    }

    @Test
    void getTasks_shouldFilterByStatusAndPriority() {
        taskService.createTask("Task 1", "desc", Status.TODO, Priority.HIGH, LocalDate.MAX);
        taskService.createTask("Task 2", "desc", Status.DONE, Priority.HIGH, LocalDate.MAX);
        taskService.createTask("Task 3", "desc", Status.DONE, Priority.LOW, LocalDate.MAX);

        List<Task> filtered = taskService.getTasks(Status.DONE, Priority.HIGH);

        assertEquals(1, filtered.size());
        assertEquals(Status.DONE, filtered.getFirst().getStatus());
        assertEquals(Priority.HIGH, filtered.getFirst().getPriority());
    }

    @Test
    void updateStatus_withNull_shouldFallbackToTodo() {
        Task task = taskService.createTask("Titre", "Desc", Status.DONE, Priority.HIGH, LocalDate.MAX);

        taskService.updateStatus(task.getId(), null);

        assertEquals(Status.TODO, task.getStatus());
    }

    @Test
    void updatePriority_withNull_shouldFallbackToMedium() {
        Task task = taskService.createTask("Titre", "Desc", Status.DONE, Priority.HIGH, LocalDate.MAX);

        taskService.updatePriority(task.getId(), null);

        assertEquals(Priority.MEDIUM, task.getPriority());
    }

    @Test
    void updateDueDate_withNull_shouldFallbackToMax() {
        Task task = taskService.createTask("Titre", "Desc", Status.DONE, Priority.HIGH, LocalDate.of(2026, 1, 1));

        taskService.updateDueDate(task.getId(), null);

        assertEquals(LocalDate.MAX, task.getDueDate());
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
        java.lang.reflect.Field field = UpdateTaskRequest.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(request, value);
    }

}