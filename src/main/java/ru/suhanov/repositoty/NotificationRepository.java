package ru.suhanov.repositoty;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.suhanov.model.bot.Notification;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
