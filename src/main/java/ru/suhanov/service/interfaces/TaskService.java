package ru.suhanov.service.interfaces;

import ru.suhanov.model.Member;
import ru.suhanov.model.task.Task;
import ru.suhanov.model.User;

public interface TaskService {
    void addNewTask(Task task);
    Task findTaskById(long id);
    Member findMemberByUserAndTaskId(User user, long taskId);
}
