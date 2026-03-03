package com.myapp.service;

import com.myapp.dto.UpdateTaskRequest;
import com.myapp.exception.TaskNotFoundException;
import com.myapp.model.Priority;
import com.myapp.model.Status;
import com.myapp.model.Task;
import com.myapp.repository.TaskRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
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

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Task getTaskById(Integer id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
    }

    public Task updateTask(Integer id, UpdateTaskRequest request) {
        Task task = getTaskById(id);

        task.patch(
                request.getStatus(),
                request.getPriority(),
                request.getDescription(),
                request.getDueDate()
        );

        return taskRepository.save(task);
    }

    public void deleteTask(Integer id) {
        if (!taskRepository.existsById(id)) {
            throw new TaskNotFoundException(id);
        }
        taskRepository.deleteById(id);
    }

    public void deleteAllTasks() {
        taskRepository.deleteAll();
    }
}