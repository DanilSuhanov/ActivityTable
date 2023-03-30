package ru.suhanov.model.bot;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String content;

    @ManyToOne
    private TelegramUser to;
}
