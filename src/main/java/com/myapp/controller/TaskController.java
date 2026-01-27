package com.myapp.controller;

import com.myapp.dto.CreateTaskRequest;
import com.myapp.dto.UpdateTaskRequest;
import com.myapp.model.Priority;
import com.myapp.model.Status;
import com.myapp.model.Task;
import com.myapp.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody CreateTaskRequest request) {
        Task task =  taskService.createTask(
                request.getTitle(),
                request.getDescription(),
                request.getStatus(),
                request.getPriority(),
                request.getDueDate()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(task);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable int id) {
        Task task = taskService.getTaskById(id);
        if (task != null) {
            return ResponseEntity.ok(task);
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public List<Task> getAllTasks(@RequestParam(required = false) Status status, @RequestParam(required = false) Priority priority) {
        return taskService.getTasks(status, priority);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> putTaskById(@PathVariable int id, @RequestBody UpdateTaskRequest request) {
        Task updatedTask = taskService.updateTask(id, request.getDescription(), request.getStatus(), request.getPriority(), request.getDueDate());

        if (updatedTask == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(updatedTask);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Task> patchTask(@PathVariable int id, @RequestBody UpdateTaskRequest request) {
        Task updatedTask = taskService.updateTask(id, request.getDescription(), request.getStatus(), request.getPriority(), request.getDueDate());

        if (updatedTask == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(updatedTask);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable int id) {
        return taskService.deleteTask(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAllTasks() {
        taskService.deleteAllTasks();
        return ResponseEntity.noContent().build();
    }
}