package ru.suhanov.service.imp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.suhanov.model.*;
import ru.suhanov.model.enam.TaskRole;
import ru.suhanov.service.interfaces.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class PreInitService {

    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;
    private final UserService userService;
    private final TaskService taskService;
    private final MemberService memberService;

    @Autowired
    public PreInitService(PasswordEncoder passwordEncoder, RoleService roleService, UserService userService, TaskService taskService, MemberService memberService) {
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
        this.userService = userService;
        this.taskService = taskService;
        this.memberService = memberService;

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

            Task task = new Task();
            task.setTitle("Task1");
            task.setDescription("Description1");
            task.setCompleteness(getRandom());

            taskService.addNewTask(task);

            Task task2 = new Task();
            task2.setTitle("Task2");
            task2.setDescription("Description2");
            task2.setCompleteness(getRandom());

            taskService.addNewTask(task2);

            Member member = new Member();
            member.setUser(user);
            member.setTask(task);
            member.setTaskRole(TaskRole.Руководитель);

            memberService.addNewMember(member);

            Member member2 = new Member();
            member2.setUser(user);
            member2.setTask(task2);
            member2.setTaskRole(TaskRole.Исполнитель);

            memberService.addNewMember(member2);

            userService.update(user);
        }
    }

    private User getRandomUser(Role role) {
        User user = new User();
        user.setUsername("User " + getRandom());
        user.setPassword(UUID.randomUUID().toString());
        user.addRole(role);
        return user;
    }

    private int getRandom() {
        return ThreadLocalRandom.current().nextInt(0, 100);
    }
}
