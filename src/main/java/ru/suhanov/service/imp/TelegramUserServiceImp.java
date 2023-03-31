package ru.suhanov.service.imp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.suhanov.model.bot.TelegramUser;
import ru.suhanov.repositoty.TelegramUserRepository;
import ru.suhanov.service.interfaces.TelegramUserService;

import javax.transaction.Transactional;

@Service
public class TelegramUserServiceImp implements TelegramUserService {
    private TelegramUserRepository telegramUserRepository;

    @Autowired
    public TelegramUserServiceImp(TelegramUserRepository telegramUserRepository) {
        this.telegramUserRepository = telegramUserRepository;
    }

    @Override
    @Transactional
    public void add(TelegramUser telegramUser) {
        telegramUserRepository.save(telegramUser);
    }
}
