package ru.suhanov.service.imp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.suhanov.model.Member;
import ru.suhanov.model.task.Task;
import ru.suhanov.model.User;
import ru.suhanov.repositoty.TaskRepository;
import ru.suhanov.service.interfaces.TaskService;

import javax.transaction.Transactional;

@Service
public class TaskServiceImp implements TaskService {

    private final TaskRepository taskRepository;

    @Autowired
    public TaskServiceImp(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    @Transactional
    public void addNewTask(Task task) {
        taskRepository.save(task);
    }

    @Override
    public Task findTaskById(long id) {
        return taskRepository.findTaskById(id);
    }

    @Override
    @Transactional
    public Member findMemberByUserAndTaskId(User user, long taskId) {
        Task task = taskRepository.findTaskById(taskId);
        return task.getMembers().stream().filter(member -> member.getUser().equals(user))
                .findFirst().orElse(null);
    }
}
