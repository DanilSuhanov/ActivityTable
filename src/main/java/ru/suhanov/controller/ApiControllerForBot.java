package ru.suhanov.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.suhanov.exception.ExceptionInfo;
import ru.suhanov.model.User;
import ru.suhanov.model.bot.Notification;
import ru.suhanov.model.bot.TelegramUser;
import ru.suhanov.service.interfaces.NotificationService;
import ru.suhanov.service.interfaces.TelegramUserService;
import ru.suhanov.service.interfaces.UserService;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/telegram")
public class ApiControllerForBot {

    private final TelegramUserService telegramUserService;
    private final NotificationService notificationService;
    private final UserService userService;

    private final long apiKey = 79107014L;

    @Autowired
    public ApiControllerForBot(TelegramUserService telegramUserService, NotificationService notificationService, UserService userService) {
        this.telegramUserService = telegramUserService;
        this.notificationService = notificationService;
        this.userService = userService;
    }

    @GetMapping("/getNotifications/{apiKey}")
    public ResponseEntity<List<Notification>> getNotifications(@PathVariable long apiKey) {
        if (apiKey == this.apiKey) {
            return new ResponseEntity<>(notificationService.pollNotificationList(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NON_AUTHORITATIVE_INFORMATION);
        }
    }

    @GetMapping("/getUnverificatedUser/{apiKey}")
    public ResponseEntity<List<TelegramUser>> getUnverificatedUser(@PathVariable long apiKey) {
        if (apiKey == this.apiKey) {
            return new ResponseEntity<>(telegramUserService.getUnverificatedUser(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NON_AUTHORITATIVE_INFORMATION);
        }
    }

    @PostMapping("/telegramUser/new")
    public ResponseEntity<ExceptionInfo> newTelegramUser(@RequestBody String username, Principal principal) {
        User user = userService.findUserByUsername(principal.getName());

        TelegramUser telegramUser = new TelegramUser();
        telegramUser.setUsername(username);
        telegramUser.setUser(user);
        telegramUserService.add(telegramUser);

        user.setTelegramUser(telegramUser);
        userService.editUser(user);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/telegramUser/verification")
    public ResponseEntity<ExceptionInfo> verificationTelegramUser(@RequestBody TelegramUser telegramUser) {
        System.out.println("Подтверждение началось");
        telegramUserService.verification(telegramUser);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/telegramUser/get")
    public ResponseEntity<TelegramUser> getTelegramUser(Principal principal) {
        User user = userService.findUserByUsername(principal.getName());
        return new ResponseEntity<>(user.getTelegramUser(), HttpStatus.OK);
    }
}
