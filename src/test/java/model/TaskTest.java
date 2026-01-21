package model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;

import util.Constants;

class TaskTest {

    @Test
    void testTaskCreationWithDefaults() {
        Task task = new Task("Réviser Java");

        assertEquals("Réviser Java", task.getTitle());
        assertEquals(Constants.EMPTY, task.getDescription());
        assertEquals(Status.TODO, task.getStatus());
        assertEquals(Priority.MEDIUM, task.getPriority());
        assertEquals(LocalDate.MAX, task.getDueDate());
        assertTrue(task.getId() > 0);
    }

    @Test
    void testTaskCreationWithAllParameters() {
        LocalDate dueDate = LocalDate.of(2026, 2, 1);
        Task task = new Task(
                "Faire les tests",
                "Cette tache permet de tester la classe Java des taches",
                Status.IN_PROGRESS,
                Priority.HIGH,
                dueDate
        );

        assertEquals("Faire les tests", task.getTitle());
        assertEquals("Cette tache permet de tester la classe Java des taches", task.getDescription());
        assertEquals(Status.IN_PROGRESS, task.getStatus());
        assertEquals(Priority.HIGH, task.getPriority());
        assertEquals(dueDate, task.getDueDate());
    }

    @Test
    void testTitleCannotBeNull() {
        assertThrows(NullPointerException.class, () -> new Task(null));
    }

    @Test
    void testSettersUpdateValues() {
        Task task = new Task("Tâche initiale");

        task.setDescription("Nouvelle description");
        task.setStatus(Status.DONE);
        task.setPriority(Priority.HIGH);
        LocalDate newDueDate = LocalDate.of(2026, 3, 10);
        task.setDueDate(newDueDate);

        assertEquals("Nouvelle description", task.getDescription());
        assertEquals(Status.DONE, task.getStatus());
        assertEquals(Priority.HIGH, task.getPriority());
        assertEquals(newDueDate, task.getDueDate());
    }

    @Test
    void testSettersHandleNullGracefully() {
        Task task = new Task("Tâche test");

        // Les setters doivent appliquer la valeur par défaut si null
        task.setDescription(null);
        task.setStatus(null);
        task.setPriority(null);
        task.setDueDate(null);

        assertEquals(Constants.EMPTY, task.getDescription());
        assertEquals(Status.TODO, task.getStatus());
        assertEquals(Priority.MEDIUM, task.getPriority());
        assertEquals(LocalDate.MAX, task.getDueDate());
    }
}
