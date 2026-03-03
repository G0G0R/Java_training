package model;

import com.myapp.model.Priority;
import com.myapp.model.Status;
import com.myapp.model.Task;
import java.time.LocalDate;

import com.myapp.util.Constants;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    @Test
    void constructorWithTitleOnly_shouldApplyDefaults() {
        Task task = new Task("Réviser Java");

        assertEquals("Réviser Java", task.getTitle());
        assertEquals(Constants.EMPTY, task.getDescription());
        assertEquals(Status.TODO, task.getStatus());
        assertEquals(Priority.MEDIUM, task.getPriority());
        assertNull(task.getDueDate());
    }

    @Test
    void fullConstructor_shouldKeepProvidedValues() {
        LocalDate dueDate = LocalDate.of(2026, 2, 1);

        Task task = new Task(
                "Faire les tests",
                "Cette tâche sert à tester le modèle",
                Status.IN_PROGRESS,
                Priority.HIGH,
                dueDate
        );

        assertEquals("Faire les tests", task.getTitle());
        assertEquals("Cette tâche sert à tester le modèle", task.getDescription());
        assertEquals(Status.IN_PROGRESS, task.getStatus());
        assertEquals(Priority.HIGH, task.getPriority());
        assertEquals(dueDate, task.getDueDate());
    }

    @Test
    void fullConstructor_withNullOptionalValues_shouldApplyDefaults() {
        Task task = new Task("Titre", null, null, null, null);

        assertEquals(Constants.EMPTY, task.getDescription());
        assertEquals(Status.TODO, task.getStatus());
        assertEquals(Priority.MEDIUM, task.getPriority());
        assertNull(task.getDueDate());
    }

    @Test
    void constructor_withNullTitle_shouldThrowNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Task(null));
    }

    @Test
    void setters_withValidValues_shouldUpdateFields() {
        Task task = new Task("Tâche initiale");
        LocalDate newDueDate = LocalDate.of(2026, 3, 10);

        task.setDescription("Nouvelle description");
        task.setStatus(Status.DONE);
        task.setPriority(Priority.HIGH);
        task.setDueDate(newDueDate);

        assertEquals("Nouvelle description", task.getDescription());
        assertEquals(Status.DONE, task.getStatus());
        assertEquals(Priority.HIGH, task.getPriority());
        assertEquals(newDueDate, task.getDueDate());
    }

    @Test
    void setters_withNullValues_shouldApplyFallbackExceptDueDate() {
        Task task = new Task("Tâche test", "Desc", Status.DONE, Priority.HIGH, LocalDate.of(2026, 1, 1));

        task.setDescription(null);
        task.setStatus(null);
        task.setPriority(null);
        task.setDueDate(null);

        assertEquals(Constants.EMPTY, task.getDescription());
        assertEquals(Status.TODO, task.getStatus());
        assertEquals(Priority.MEDIUM, task.getPriority());
        assertNull(task.getDueDate());
    }

    @Test
    void isOverdue_shouldReturnTrueForPastDate() {
        Task task = new Task("Tâche", "Desc", Status.TODO, Priority.MEDIUM, LocalDate.now().minusDays(1));

        assertTrue(task.isOverdue());
    }

    @Test
    void isOverdue_shouldReturnFalseForTodayFutureOrNullDate() {
        Task todayTask = new Task("Today", "Desc", Status.TODO, Priority.MEDIUM, LocalDate.now());
        Task futureTask = new Task("Future", "Desc", Status.TODO, Priority.MEDIUM, LocalDate.now().plusDays(2));
        Task nullDateTask = new Task("Null date", "Desc", Status.TODO, Priority.MEDIUM, null);

        assertFalse(todayTask.isOverdue());
        assertFalse(futureTask.isOverdue());
        assertFalse(nullDateTask.isOverdue());
    }

    @Test
    void update_shouldOverwriteAllFieldsIncludingNulls() {
        Task task = new Task("Titre", "Desc", Status.IN_PROGRESS, Priority.HIGH, LocalDate.now().plusDays(5));

        task.update(null, null, null, null);

        assertNull(task.getDescription());
        assertNull(task.getStatus());
        assertNull(task.getPriority());
        assertNull(task.getDueDate());
    }

    @Test
    void patch_shouldOnlyUpdateProvidedFields() {
        Task task = new Task("Titre", "Desc", Status.TODO, Priority.MEDIUM, LocalDate.of(2026, 1, 1));

        task.patch(Status.DONE, null, "Nouvelle desc", null);

        assertEquals(Status.DONE, task.getStatus());
        assertEquals(Priority.MEDIUM, task.getPriority());
        assertEquals("Nouvelle desc", task.getDescription());
        assertEquals(LocalDate.of(2026, 1, 1), task.getDueDate());
    }

    @Test
    void patch_withAllNulls_shouldKeepCurrentState() {
        Task task = new Task("Titre", "Desc", Status.TODO, Priority.LOW, LocalDate.of(2026, 2, 2));

        task.patch(null, null, null, null);

        assertEquals("Desc", task.getDescription());
        assertEquals(Status.TODO, task.getStatus());
        assertEquals(Priority.LOW, task.getPriority());
        assertEquals(LocalDate.of(2026, 2, 2), task.getDueDate());
    }

    @Test
    void toString_shouldContainMainFields() {
        Task task = new Task("Titre", "Desc", Status.TODO, Priority.LOW, LocalDate.of(2026, 2, 2));

        String value = task.toString();

        assertNotNull(value);
        assertTrue(value.contains("title='Titre'"));
        assertTrue(value.contains("description='Desc'"));
        assertTrue(value.contains("status=TODO"));
        assertTrue(value.contains("priority=LOW"));
    }
}