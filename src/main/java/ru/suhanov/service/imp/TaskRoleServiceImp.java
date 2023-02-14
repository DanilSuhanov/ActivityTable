package ru.suhanov.service.imp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.suhanov.model.TaskRole;
import ru.suhanov.repositoty.TaskRoleRepository;
import ru.suhanov.service.interfaces.TaskRoleService;

@Service
public class TaskRoleServiceImp implements TaskRoleService {

    private final TaskRoleRepository taskRoleRepository;

    @Autowired
    public TaskRoleServiceImp(TaskRoleRepository taskRoleRepository) {
        this.taskRoleRepository = taskRoleRepository;
    }

    @Override
    public void addNewTaskRole(TaskRole taskRole) {
        taskRoleRepository.save(taskRole);
    }
}
