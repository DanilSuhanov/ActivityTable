package ru.suhanov.repositoty;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.suhanov.model.TaskRole;

@Repository
public interface TaskRoleRepository extends JpaRepository<TaskRole, Long> {
}
