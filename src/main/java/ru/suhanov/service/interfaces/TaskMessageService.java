package ru.suhanov.service.interfaces;

import ru.suhanov.model.task.TaskMessage;

public interface TaskMessageService {
    void addNewTaskMessage(TaskMessage taskMessage);
    TaskMessage findTaskMessageById(long id);
    void deleteTaskMessageById(long id);
}
