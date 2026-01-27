package com.myapp.service;

import com.myapp.model.Priority;
import com.myapp.model.Status;
import com.myapp.model.Task;
import com.myapp.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public Task createTask(String title) {
        Task task = new Task(title);
        return taskRepository.save(task);
    }

    public Task createTask(
            String title,
            String description,
            Status status,
            Priority priority,
            LocalDate dueDate
    ) {
        Task task = new Task(title, description, status, priority, dueDate);
        return taskRepository.save(task);
    }

    public Task getTaskById(int id) {
        return taskRepository.findById(id)
                .orElse(null);
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public boolean deleteTask(int id) {
        return taskRepository.deleteById(id);
    }

    public void deleteAllTasks() {
        taskRepository.deleteAll();
    }

    public void updateDescription(int id, String newDescription) {
        Task task = getTaskById(id);
        task.setDescription(newDescription);
        taskRepository.save(task);
    }

    public void updateStatus(int id, Status newStatus) {
        Task task = getTaskById(id);
        task.setStatus(newStatus);
        taskRepository.save(task);
    }

    public void updatePriority(int id, Priority newPriority) {
        Task task = getTaskById(id);
        task.setPriority(newPriority);
        taskRepository.save(task);
    }

    public void updateDueDate(int id, LocalDate newDueDate) {
        Task task = getTaskById(id);
        task.setDueDate(newDueDate);
        taskRepository.save(task);
    }

    public Task updateTask(int id, String description, Status status, Priority priority, LocalDate dueDate) {
        Optional<Task> optionalTask = taskRepository.findById(id);
        if (optionalTask.isEmpty()) {
            return null;
        }
        Task task = optionalTask.get();
        task.update(description, status, priority, dueDate);
        return taskRepository.save(task);
    }

    public List<Task> getTasks(Status status, Priority priority) {
        return taskRepository.findAll().stream()
                .filter(task -> status == null || task.getStatus() == status)
                .filter(task -> priority == null || task.getPriority() == priority)
                .collect(Collectors.toList());
    }
}