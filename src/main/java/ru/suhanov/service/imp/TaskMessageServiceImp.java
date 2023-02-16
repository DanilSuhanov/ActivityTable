package ru.suhanov.service.imp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.suhanov.model.task.TaskMessage;
import ru.suhanov.repositoty.TaskMessageRepository;
import ru.suhanov.service.interfaces.TaskMessageService;

import javax.transaction.Transactional;

@Service
public class TaskMessageServiceImp implements TaskMessageService {

    private final TaskMessageRepository taskMessageRepository;

    @Autowired
    public TaskMessageServiceImp(TaskMessageRepository taskMessageRepository) {
        this.taskMessageRepository = taskMessageRepository;
    }

    @Override
    @Transactional
    public void addNewTaskMessage(TaskMessage taskMessage) {
        taskMessageRepository.save(taskMessage);
    }
}
