package com.myapp.repository;

import com.myapp.model.Task;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class InMemoryTaskRepository implements TaskRepository {

    private final Map<Integer, Task> tasks = new HashMap<>();

    @Override
    public Task save(Task task) {
        tasks.put(task.getId(), task);
        return task;
    }

    @Override
    public Optional<Task> findById(int id) {
        return Optional.ofNullable(tasks.get(id));
    }

    @Override
    public List<Task> findAll() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public boolean deleteById(int id) {
        return tasks.remove(id) != null;
    }

    @Override
    public void deleteAll() {
        tasks.clear();
    }
}