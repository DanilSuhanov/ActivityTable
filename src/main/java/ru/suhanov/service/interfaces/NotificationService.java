package ru.suhanov.service.interfaces;

import ru.suhanov.model.bot.Notification;

import java.util.List;

public interface NotificationService {
    void add(Notification notification);
    List<Notification> pollNotificationList();
}
