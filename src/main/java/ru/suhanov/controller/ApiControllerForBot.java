package ru.suhanov.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.suhanov.model.bot.Notification;
import ru.suhanov.model.bot.TelegramUser;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ApiControllerForBot {

    private List<Notification> notifications;

    public ApiControllerForBot() {
        notifications = new ArrayList<>();

        TelegramUser telegramUser = new TelegramUser();
        telegramUser.setUsername("username");
        telegramUser.setChatId( 1019574601L);
        telegramUser.setFirstName("Данил");
        telegramUser.setLastName("Суханов");
        telegramUser.setId(0L);

        Notification notification = new Notification();
        notification.setContent("Привет!");
        notification.setTo(telegramUser);
        notification.setId(0L);

        Notification notification2 = new Notification();
        notification2.setContent("Как дела?");
        notification2.setTo(telegramUser);
        notification2.setId(1L);

        notifications.add(notification);
        notifications.add(notification2);
    }

    @GetMapping("/getNotifications")
    public synchronized ResponseEntity<List<Notification>> getNotifications() {
        ResponseEntity<List<Notification>> re = new ResponseEntity<>(notifications, HttpStatus.OK);
        notifications.clear();
        return re;
    }
}
