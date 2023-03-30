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

    @GetMapping("/getNotifications")
    public ResponseEntity<List<Notification>> getNotifications() {
        List<Notification> notifications = new ArrayList<>();

        TelegramUser telegramUser = new TelegramUser();
        telegramUser.setUsername("username");
        telegramUser.setChatId( 1019574601L);
        telegramUser.setFirstName("Данил");
        telegramUser.setLastName("Суханов");

        Notification notification = new Notification();
        notification.setContent("Привет!");
        notification.setTo(telegramUser);

        Notification notification2 = new Notification();
        notification2.setContent("Как дела?");
        notification2.setTo(telegramUser);

        notifications.add(notification);
        notifications.add(notification2);

        return new ResponseEntity<>(notifications, HttpStatus.OK);
    }
}
