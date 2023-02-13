package ru.suhanov.service.imp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.suhanov.model.Role;
import ru.suhanov.model.User;
import ru.suhanov.service.interfaces.RoleService;
import ru.suhanov.service.interfaces.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class PreInitService {

    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;
    private final UserService userService;

    @Autowired
    public PreInitService(PasswordEncoder passwordEncoder, RoleService roleService, UserService userService) {
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
        this.userService = userService;

        preInit();
    }

    private String generateString() {
        return passwordEncoder.encode("pass");
    }

    public void preInit() {
        Role adminRole = null;
        Role userRole = null;

        String authorityAdmin = "ROLE_ADMIN";
        String authorityUser = "ROLE_USER";

        if (roleService.findRoleByAuthority(authorityAdmin) == null) {
            adminRole = new Role();
            adminRole.setAuthority(authorityAdmin);

            roleService.addNewRole(adminRole);
        }

        if (roleService.findRoleByAuthority(authorityUser) == null) {
            userRole = new Role();
            userRole.setAuthority(authorityUser);

            roleService.addNewRole(userRole);
        }

        adminRole = roleService.findRoleByAuthority(authorityAdmin);
        userRole = roleService.findRoleByAuthority(authorityUser);

        if (userService.findUserByUsername("admin") == null) {
            User user = new User();
            user.setUsername("admin");
            user.setPassword(generateString());

            user.addRole(adminRole);
            user.addRole(userRole);

            userService.addNewUser(user);

            List<User> subs = new ArrayList<>();
            for (int i = 0; i < 3; i++) {
                subs.add(getRandomUser(userRole));
                userService.addNewUser(subs.get(i));
                user.addSubordinates(subs.get(i));
            }

            userService.update(user);
        }
    }

    private User getRandomUser(Role role) {
        User user = new User();
        user.setUsername("User " + ThreadLocalRandom.current().nextInt(0, 100));
        user.setPassword(UUID.randomUUID().toString());
        user.addRole(role);
        return user;
    }
}
