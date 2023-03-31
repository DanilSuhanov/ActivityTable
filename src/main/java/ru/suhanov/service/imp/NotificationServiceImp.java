package ru.suhanov.service.imp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.suhanov.model.bot.Notification;
import ru.suhanov.repositoty.NotificationRepository;
import ru.suhanov.service.interfaces.NotificationService;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class NotificationServiceImp implements NotificationService {
    private final NotificationRepository notificationRepository;
    @Autowired
    public NotificationServiceImp(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Override
    @Transactional
    public void add(Notification notification) {
        notificationRepository.save(notification);
    }

    @Override
    @Transactional
    public List<Notification> pollNotificationList() {
        List<Notification> notifications = notificationRepository.findAll();
        if (notifications.size() > 0) {
            notificationRepository.deleteAll();
        }
        return notifications;
    }
}
