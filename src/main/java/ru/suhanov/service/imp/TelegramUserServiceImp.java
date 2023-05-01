package ru.suhanov.service.imp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.suhanov.model.bot.TelegramUser;
import ru.suhanov.repositoty.TelegramUserRepository;
import ru.suhanov.service.interfaces.TelegramUserService;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class TelegramUserServiceImp implements TelegramUserService {
    private final TelegramUserRepository telegramUserRepository;

    @Autowired
    public TelegramUserServiceImp(TelegramUserRepository telegramUserRepository) {
        this.telegramUserRepository = telegramUserRepository;
    }

    @Override
    public void add(TelegramUser telegramUser) {
        telegramUserRepository.save(telegramUser);
    }

    @Override
    public void verification(TelegramUser telegramUser) {
        TelegramUser telegramUserOnDb = telegramUserRepository.findByUsername(telegramUser.getUsername());
        telegramUserOnDb.fill(telegramUser);
        telegramUserRepository.save(telegramUserOnDb);
    }

    @Override
    public List<TelegramUser> getUnverificatedUser() {
        return telegramUserRepository.findAll().stream().filter(telegramUser -> telegramUser.getChatId() == null)
                .collect(Collectors.toList());
    }
}
