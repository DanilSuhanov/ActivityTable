package ru.suhanov.service.imp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.suhanov.Util;
import ru.suhanov.model.Member;
import ru.suhanov.model.task.Task;
import ru.suhanov.model.User;
import ru.suhanov.model.task.TaskMessage;
import ru.suhanov.repositoty.TaskRepository;
import ru.suhanov.service.interfaces.NotificationService;
import ru.suhanov.service.interfaces.TaskService;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class TaskServiceImp implements TaskService {

    private final TaskRepository taskRepository;
    private final NotificationService notificationService;

    @Autowired
    public TaskServiceImp(TaskRepository taskRepository, NotificationService notificationService) {
        this.taskRepository = taskRepository;
        this.notificationService = notificationService;
    }

    @Override
    public void addNewTask(Task task) {
        if (findTaskByTitle(task.getTitle()) == null) {
            taskRepository.save(task);
        }
    }

    @Override
    public Task findTaskById(long id) {
        return taskRepository.findTaskById(id);
    }

    @Override
    public Member findMemberByUserAndTaskId(User user, long taskId) {
        Task task = taskRepository.findTaskById(taskId);
        return task.getMembers().stream().filter(member -> member.getUser().equals(user))
                .findFirst().orElse(null);
    }

    @Override
    public Member findMemberByUserAndTaskId(User user, Task task) {
        return task.getMembers().stream().filter(member -> member.getUser().equals(user))
                .findFirst().orElse(null);
    }

    @Override
    public List<TaskMessage> findAllMessagesByTaskId(long id) {
        return taskRepository.findTaskById(id).getTaskMessages();
    }

    @Override
    public void update(Task task) {
        taskRepository.save(task);
    }

    @Override
    public Task findTaskByTitle(String title) {
        return taskRepository.findTaskByTitle(title);
    }

    @Override
    public void deleteTask(Task task) {
        taskRepository.delete(task);
    }

    @Override
    public void checkAllTasks() {
        List<Task> tasks = taskRepository.findTaskByExpired(false);
        checkOnExpired(tasks);
        checkNotification(tasks);
    }

    private void checkNotification(List<Task> tasks) {
        for (Task task : tasks) {
            if (task.getDeadline().isAfter(LocalDateTime.now().minusDays(1))) {
                for (User user : task.getMembers().stream().map(Member::getUser).collect(Collectors.toList())) {
                    notificationService.notification("Скоро истечёт срок выполнения задачи - "
                            + task.getTitle(), user);
                }
            }
        }
    }

    private void checkOnExpired(List<Task> tasks) {
        for (Task task : tasks) {
            if (task.getDeadline().isBefore(LocalDateTime.now().minusDays(1))) {
                task.setExpired(true);
                taskRepository.save(task);
            }
        }
    }
}
