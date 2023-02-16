package ru.suhanov.repositoty;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.suhanov.model.task.TaskMessage;

@Repository
public interface TaskMessageRepository extends JpaRepository<TaskMessage, Long> {
}
