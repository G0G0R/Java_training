package com.myapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.myapp.model.Task;

public interface TaskRepository extends JpaRepository<Task, Integer> {
}