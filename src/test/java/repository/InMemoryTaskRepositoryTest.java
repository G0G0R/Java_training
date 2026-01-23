package repository;

import com.myapp.model.Task;
import com.myapp.repository.InMemoryTaskRepository;
import com.myapp.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = InMemoryTaskRepository.class)
class InMemoryTaskRepositoryTest {

    @Autowired
    private TaskRepository taskRepository;

    @BeforeEach
    void setUp() {
        taskRepository.deleteAll(); // vide le repository avant chaque test
    }

    @Test
    void saveAndFindById_shouldWork() {
        Task task = new Task("Test");
        taskRepository.save(task);

        Optional<Task> found = taskRepository.findById(task.getId());

        assertTrue(found.isPresent());
        assertEquals("Test", found.get().getTitle());
    }

    @Test
    void deleteById_shouldRemoveTask() {
        Task task = new Task("Test");
        taskRepository.save(task);

        taskRepository.deleteById(task.getId());

        assertTrue(taskRepository.findById(task.getId()).isEmpty());
    }

    @Test
    void findAll_shouldReturnAllTasks() {
        Task t1 = new Task("Task 1");
        Task t2 = new Task("Task 2");
        taskRepository.save(t1);
        taskRepository.save(t2);

        List<Task> tasks = taskRepository.findAll();
        assertEquals(2, tasks.size());
        assertTrue(tasks.contains(t1));
        assertTrue(tasks.contains(t2));
    }

    @Test
    void deleteAll_shouldClearRepository() {
        Task t1 = new Task("Task 1");
        Task t2 = new Task("Task 2");
        taskRepository.save(t1);
        taskRepository.save(t2);

        taskRepository.deleteAll();
        List<Task> tasks = taskRepository.findAll();
        assertTrue(tasks.isEmpty());
    }
}
