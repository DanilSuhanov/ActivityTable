package ru.suhanov.model.bot;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import ru.suhanov.model.User;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class TelegramUser {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long chatId;
    private String username;
    private String firstName;
    private String lastName;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Notification> notifications = new ArrayList<>();

    @JsonIgnore
    @OneToOne(mappedBy = "telegramUser", cascade = CascadeType.ALL)
    private User user;

    public void fill(TelegramUser telegramUser) {
        chatId = telegramUser.getChatId();
        firstName = telegramUser.getFirstName();
        lastName = telegramUser.getLastName();
    }
}
