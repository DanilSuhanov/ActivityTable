package ru.suhanov.repositoty;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.suhanov.model.task.Task;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    Task findTaskById(long id);
    Task findTaskByTitle(String title);
    List<Task> findTasksByExpired(boolean expired);
}
