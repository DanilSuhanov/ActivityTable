package ru.suhanov.service.imp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.suhanov.Util;
import ru.suhanov.model.Member;
import ru.suhanov.model.task.Task;
import ru.suhanov.model.User;
import ru.suhanov.model.task.TaskMessage;
import ru.suhanov.repositoty.TaskRepository;
import ru.suhanov.service.interfaces.MemberService;
import ru.suhanov.service.interfaces.TaskService;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class TaskServiceImp implements TaskService {

    private final TaskRepository taskRepository;
    private final MemberService memberService;

    @Autowired
    public TaskServiceImp(TaskRepository taskRepository, MemberService memberService) {
        this.taskRepository = taskRepository;
        this.memberService = memberService;
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
    public void checkAllTasksOnExpired() {
        System.out.println("Чек метод начал выполняться! " + Util.dateToString(LocalDateTime.now()));
        int counter = 0;
        List<Task> tasks = taskRepository.findTasksByExpired(false);
        for (Task task : tasks) {
            if (task.getDeadline().isBefore(LocalDateTime.now())) {
                counter++;
                task.setExpired(true);
                taskRepository.save(task);
            }
        }
        System.out.println("Чек метод закончил выполняться! Новых просроченных задач - " + counter
                + ". Время - " + Util.dateToString(LocalDateTime.now()));
    }
}
