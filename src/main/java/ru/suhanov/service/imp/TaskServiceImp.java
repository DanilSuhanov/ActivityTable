package ru.suhanov.service.imp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.suhanov.model.Task;
import ru.suhanov.repositoty.TaskRepository;
import ru.suhanov.service.interfaces.TaskService;

@Service
public class TaskServiceImp implements TaskService {

    private final TaskRepository taskRepository;

    @Autowired
    public TaskServiceImp(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public void addNewTask(Task task) {
        taskRepository.save(task);
    }
}
