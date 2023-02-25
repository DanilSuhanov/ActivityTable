package ru.suhanov.service.imp;

import org.apache.logging.log4j.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.suhanov.model.Member;
import ru.suhanov.model.task.Task;
import ru.suhanov.model.User;
import ru.suhanov.model.task.TaskMessage;
import ru.suhanov.repositoty.TaskRepository;
import ru.suhanov.service.interfaces.TaskService;

import javax.transaction.Transactional;
import java.util.List;

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
        if (findTaskByTitle(task.getTitle()) == null) {
            taskRepository.save(task);
        }
    }

    @Override
    @Transactional
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

    @Override
    @Transactional
    public List<TaskMessage> findAllMessagesByTaskId(long id) {
        return taskRepository.findTaskById(id).getTaskMessages();
    }

    @Override
    @Transactional
    public void update(Task task) {
        taskRepository.save(task);
    }

    @Override
    @Transactional
    public Task findTaskByTitle(String title) {
        return taskRepository.findTaskByTitle(title);
    }
}
