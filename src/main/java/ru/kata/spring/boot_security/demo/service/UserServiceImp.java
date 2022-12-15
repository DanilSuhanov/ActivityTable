package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repositoty.UserRepository;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class UserServiceImp implements UserService{

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

    @Override
    @Transactional
    public void editUser(User user) {
        User oldUser = userRepository.findById(user.getId()).orElse(null);
        if (oldUser != null) {
            oldUser.setRoles(user.getRoles());
            oldUser.setEmail(user.getEmail());
            oldUser.setPassword(user.getPassword());
            oldUser.setUsername(user.getUsername());
            userRepository.save(oldUser);
        }
    }

    @Override
    @Transactional
    public User findUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
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

    private boolean exist(Long id) {
        return userRepository.findById(id).isPresent();
    }
}
