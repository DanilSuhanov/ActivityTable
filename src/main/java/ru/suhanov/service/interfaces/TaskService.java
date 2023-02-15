package ru.suhanov.service.interfaces;

import ru.suhanov.model.Member;
import ru.suhanov.model.Task;
import ru.suhanov.model.User;

public interface TaskService {
    void addNewTask(Task task);
    Member findMemberByUserAndTaskId(User user, long taskId);
}
