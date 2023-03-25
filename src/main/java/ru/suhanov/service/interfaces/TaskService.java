package ru.suhanov.service.interfaces;

import ru.suhanov.model.Member;
import ru.suhanov.model.task.Task;
import ru.suhanov.model.User;
import ru.suhanov.model.task.TaskMessage;

import java.util.List;

public interface TaskService {
    void addNewTask(Task task);
    Task findTaskById(long id);
    Member findMemberByUserAndTaskId(User user, long taskId);
    Member findMemberByUserAndTaskId(User user, Task task);
    List<TaskMessage> findAllMessagesByTaskId(long id);
    void update(Task task);
    Task findTaskByTitle(String title);

    void deleteTask(Task task);
}
