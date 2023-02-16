package ru.suhanov.service.interfaces;

import ru.suhanov.model.task.Task;
import ru.suhanov.model.User;

import java.util.List;

public interface UserService {
    void addNewUser(User user);
    void editUser(User user);
    User findUserById(Long id);
    User findUserByUsername(String username);
    List<User> findAll();
    void deleteUserById(Long id);
    void update(User user);
    List<Task> parsUser(String name);
}
