package ru.suhanov.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.suhanov.model.bot.Notification;
import ru.suhanov.service.interfaces.NotificationService;
import ru.suhanov.service.interfaces.TelegramUserService;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ApiControllerForBot {

    private final TelegramUserService telegramUserService;
    private final NotificationService notificationService;

    @Autowired
    public ApiControllerForBot(TelegramUserService telegramUserService, NotificationService notificationService) {
        this.telegramUserService = telegramUserService;
        this.notificationService = notificationService;
    }

    @GetMapping("/getNotifications")
    public ResponseEntity<List<Notification>> getNotifications() {
        return new ResponseEntity<>(notificationService.pollNotificationList(), HttpStatus.OK);
    }
}
