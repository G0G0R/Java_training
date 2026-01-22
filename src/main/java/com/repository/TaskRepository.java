package com.repository;

import com.model.Task;

import java.util.List;
import java.util.Optional;

public interface TaskRepository {

    Task save(Task task);

    Optional<Task> findById(int id);

    List<Task> findAll();

    void deleteById(int id);

    void deleteAll();
}