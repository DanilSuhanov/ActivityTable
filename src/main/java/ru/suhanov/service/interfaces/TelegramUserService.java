package ru.suhanov.service.interfaces;

import ru.suhanov.model.bot.TelegramUser;

import java.util.List;

public interface TelegramUserService {
    void add(TelegramUser telegramUser);
    void verification(TelegramUser telegramUser);
    List<TelegramUser> getUnverificatedUser();
}
