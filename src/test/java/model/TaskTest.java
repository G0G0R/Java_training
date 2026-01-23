package model;

import com.myapp.model.Priority;
import com.myapp.model.Status;
import com.myapp.model.Task;
import java.time.LocalDate;

import com.myapp.util.Constants;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TaskTest {

    @Test
    void testTaskCreationWithDefaults() {
        Task task = new Task("Réviser Java");

        Assertions.assertEquals("Réviser Java", task.getTitle());
        Assertions.assertEquals(Constants.EMPTY, task.getDescription());
        Assertions.assertEquals(Status.TODO, task.getStatus());
        Assertions.assertEquals(Priority.MEDIUM, task.getPriority());
        Assertions.assertEquals(LocalDate.MAX, task.getDueDate());
        Assertions.assertTrue(task.getId() > 0);
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

        Assertions.assertEquals("Faire les tests", task.getTitle());
        Assertions.assertEquals("Cette tache permet de tester la classe Java des taches", task.getDescription());
        Assertions.assertEquals(Status.IN_PROGRESS, task.getStatus());
        Assertions.assertEquals(Priority.HIGH, task.getPriority());
        Assertions.assertEquals(dueDate, task.getDueDate());
    }

    @Test
    void testTitleCannotBeNull() {
        Assertions.assertThrows(NullPointerException.class, () -> new Task(null));
    }

    @Test
    void testSettersUpdateValues() {
        Task task = new Task("Tâche initiale");

        task.setDescription("Nouvelle description");
        task.setStatus(Status.DONE);
        task.setPriority(Priority.HIGH);
        LocalDate newDueDate = LocalDate.of(2026, 3, 10);
        task.setDueDate(newDueDate);

        Assertions.assertEquals("Nouvelle description", task.getDescription());
        Assertions.assertEquals(Status.DONE, task.getStatus());
        Assertions.assertEquals(Priority.HIGH, task.getPriority());
        Assertions.assertEquals(newDueDate, task.getDueDate());
    }

    @Test
    void testSettersHandleNullGracefully() {
        Task task = new Task("Tâche test");

        // Les setters doivent appliquer la valeur par défaut si null
        task.setDescription(null);
        task.setStatus(null);
        task.setPriority(null);
        task.setDueDate(null);

        Assertions.assertEquals(Constants.EMPTY, task.getDescription());
        Assertions.assertEquals(Status.TODO, task.getStatus());
        Assertions.assertEquals(Priority.MEDIUM, task.getPriority());
        Assertions.assertEquals(LocalDate.MAX, task.getDueDate());
    }

    @Test
    void testDateIsOverdue() {
        Task task = new Task("Tache test");

        task.setDueDate(LocalDate.now().plusDays(1));

        Assertions.assertTrue(task.isOverdue());
    }

    @Test
    void testDateIsNotOverdue() {
        Task task = new Task("Tache test");

        task.setDueDate(LocalDate.now().plusDays(-1));

        Assertions.assertFalse(task.isOverdue());
    }

}
