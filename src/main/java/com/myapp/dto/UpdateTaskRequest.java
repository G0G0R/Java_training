package com.myapp.dto;

import com.myapp.model.Priority;
import com.myapp.model.Status;

import java.time.LocalDate;

public class UpdateTaskRequest {

    private String description;
    private Status status;
    private Priority priority;
    private LocalDate dueDate;

    public String getDescription() {
        return description;
    }

    public Status getStatus() {
        return status;
    }

    public Priority getPriority() {
        return priority;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }
}
