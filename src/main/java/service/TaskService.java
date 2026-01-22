package service;

import model.Priority;
import model.Status;
import model.Task;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskService {
    private final Map<Integer, Task> tasksById = new HashMap<>();
    private final List<Task> tasksList = new ArrayList<>();

    public Task createTask(String title) {
        Task t = new Task(title);
        tasksById.put(t.getId(), t);
        tasksList.add(t);
        return t;
    }

    public Task createTask (String title, String description, Status status, Priority priority, LocalDate dueDate) {
        Task t = new Task(title, description, status, priority, dueDate);
        tasksById.put(t.getId(), t);
        tasksList.add(t);
        return t;
    }

    public Task getTaskById(int id) {
        return tasksById.get(id);
    }

    public List<Task> getAllTasks() {
        return new ArrayList<>(tasksList); // copie pour sécurité
    }

    public boolean deleteTask(int id) {
        Task task = tasksById.remove(id);

        if (task == null) {
            return false;
        }

        tasksList.remove(task);
        return true;
    }

    public void deleteAllTasks() {
        tasksById.clear();
        tasksList.clear();
    }

    // --- mises à jour ---
    public void updateTaskDescription(int id, String description) {
        Task task = getExistingTask(id);
        task.setDescription(description);
    }

    public void updateTaskStatus(int id, Status status) {
        Task task = getExistingTask(id);
        task.setStatus(status);
    }

    public void updateTaskPriority(int id, Priority priority) {
        Task task = getExistingTask(id);
        task.setPriority(priority);
    }

    public void updateTaskDueDate(int id, LocalDate dueDate) {
        Task task = getExistingTask(id);
        task.setDueDate(dueDate);
    }
    private Task getExistingTask(int id) {
        Task task = tasksById.get(id);
        if (task == null) {
            throw new IllegalArgumentException("Task with id " + id + " not found");
        }
        return task;
    }

}
