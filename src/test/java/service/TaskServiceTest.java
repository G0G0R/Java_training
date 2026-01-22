package service;

import com.model.Priority;
import com.model.Status;
import com.model.Task;
import com.repository.InMemoryTaskRepository;
import com.service.TaskService;
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

        boolean deleted = taskService.deleteTask(task.getId());

        Assertions.assertTrue(deleted);
        assertThrows(IllegalArgumentException.class, () -> taskService.getTaskById(task.getId()));
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
                IllegalArgumentException.class,
                () -> taskService.updateStatus(999, Status.DONE)
        );
    }
}