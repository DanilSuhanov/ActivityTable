package ru.suhanov.service.imp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.suhanov.model.Member;
import ru.suhanov.model.task.Task;
import ru.suhanov.model.User;
import ru.suhanov.repositoty.UserRepository;
import ru.suhanov.service.interfaces.UserService;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImp implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImp(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public void addNewUser(User user) {
        if (userRepository.findUserByUsername(user.getUsername()) == null) {
            userRepository.save(user);
        }
    }

    public List<Task> parsUser(String name) {
        return findUserByUsername(name).getMembers().stream().map(Member::getTask).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void editUser(User user) {
        User oldUser = userRepository.findById(user.getId()).orElse(null);
        if (oldUser != null) {
            oldUser.setRoles(user.getRoles());
            oldUser.setPassword(user.getPassword());
            oldUser.setUsername(user.getUsername());
            oldUser.setTelegramUser(user.getTelegramUser());
            userRepository.save(oldUser);
        }
    }

    @Override
    @Transactional
    public User findUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public User findUserByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }

    @Override
    @Transactional
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    @Transactional
    public void deleteUserById(Long id) {
        if (exist(id))
            userRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void update(User user) {
        if (exist(user.getId()))
            userRepository.save(user);
    }

    @Override
    @Transactional
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    private boolean exist(Long id) {
        return userRepository.findById(id).isPresent();
    }
}
