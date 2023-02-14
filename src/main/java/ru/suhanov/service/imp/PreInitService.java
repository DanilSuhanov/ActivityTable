package ru.suhanov.service.imp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.suhanov.model.*;
import ru.suhanov.model.enam.TaskRoleAuthority;
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
    private final TaskRoleService taskRoleService;
    private final MemberService memberService;

    @Autowired
    public PreInitService(PasswordEncoder passwordEncoder, RoleService roleService, UserService userService, TaskService taskService, TaskRoleService taskRoleService, MemberService memberService) {
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
        this.userService = userService;
        this.taskService = taskService;
        this.taskRoleService = taskRoleService;
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
            task.setCompleteness(50);

            taskService.addNewTask(task);

            TaskRole taskRole = new TaskRole();
            taskRole.setRoleAuthority(TaskRoleAuthority.Executive);
            taskRole.setTitle("Developer");
            taskRole.setTask(task);

            taskRoleService.addNewTaskRole(taskRole);

            Member member = new Member();
            member.setUser(user);
            member.setTask(task);
            member.addTaskRole(taskRole);

            memberService.addNewMember(member);

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
