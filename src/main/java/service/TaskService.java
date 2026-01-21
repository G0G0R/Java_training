package service;

import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskService {
    private final Map<Integer, Task> tasksById = new HashMap<>();
    private final List<Task> tasksList = new ArrayList<>();

    public void createTask(String title) {
        Task t = new Task(title);
        tasksById.put(t.getId(), t);
        tasksList.add(t);
    }

    public Task getTaskById(int id) {
        return tasksById.get(id);
    }

    public List<Task> getAllTasks() {
        return new ArrayList<>(tasksList); // copie pour sécurité
    }
}
