package ru.suhanov.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import ru.suhanov.service.interfaces.TaskService;

@Configuration
@EnableScheduling
public class SchedulerConfig {

    private final TaskService taskService;

    @Autowired
    public SchedulerConfig(TaskService taskService) {
        this.taskService = taskService;
    }

    @Scheduled(initialDelay = 0, fixedDelay = 3600000)
    public void scheduleTask() {
        taskService.checkAllTasksOnExpired();
    }
}