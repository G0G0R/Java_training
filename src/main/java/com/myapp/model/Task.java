package com.myapp.model;

import com.myapp.util.Constants;

import java.time.LocalDate;
import java.util.Objects;

public class Task {
    private static int counter = 1;
    private final int id;
    private final String title;
    private String description;
    private Status status;
    private Priority priority;
    private LocalDate dueDate;

    // Constructeur complet
    public Task(String title, String description,  Status status, Priority priority, LocalDate dueDate) {
        this.id = counter++;
        // Validation du titre obligatoire
        this.title = Objects.requireNonNull(title, "Le titre est obligatoire");
        // Valeurs par défaut si null
        this.description = (description != null) ? description : Constants.EMPTY;
        this.status = (status != null) ? status : Status.TODO;
        this.priority = (priority != null) ? priority : Priority.MEDIUM;
        this.dueDate = (dueDate != null) ? dueDate : LocalDate.MAX;
    }

    // Constructeur simplifié : seulement le titre
    public Task(String title) {
        this(title, Constants.EMPTY,  Status.TODO, Priority.MEDIUM, LocalDate.MAX);
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = (description != null) ? description : Constants.EMPTY;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = (status != null) ? status : Status.TODO;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = (priority != null) ? priority : Priority.MEDIUM;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = (dueDate != null) ? dueDate : LocalDate.MAX;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", priority=" + priority +
                ", dueDate=" + dueDate +
                '}';
    }

    public boolean isOverdue(){
        return dueDate.isAfter(LocalDate.now());
    }
}
