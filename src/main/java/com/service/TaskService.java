package com.service;

import com.model.Priority;
import com.model.Status;
import com.model.Task;
import com.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

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
                .orElseThrow(() -> new IllegalArgumentException("Task not found"));
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public boolean deleteTask(int id) {
        if (taskRepository.findById(id).isEmpty()) {
            return false;
        }
        taskRepository.deleteById(id);
        return true;
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
}