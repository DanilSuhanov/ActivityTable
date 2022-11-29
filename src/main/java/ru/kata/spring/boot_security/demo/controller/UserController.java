package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.security.Principal;
import java.util.HashSet;
import java.util.Set;

@Controller
public class UserController {

    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public UserController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("/admin/allUsers")
    public String index(Model model) {
        model.addAttribute("users", userService.findAll());
        return "allUsers";
    }

    @GetMapping("/admin/new")
    private String newUser(Model model) {
        model.addAttribute("user", new User());
        return "new";
    }

    @PostMapping("/admin/new")
    private String saveUser(User user) {
        Set<Role> roles = new HashSet<>();
        roles.add(roleService.findRoleByAuthority("ROLE_USER"));
        user.setRoles(roles);
        userService.addNewUser(user);
        return "redirect:/";
    }

    @GetMapping("/user")
    private String profile(Principal principal, Model model) {
        model.addAttribute("user", userService.findUserByUsername(principal.getName()));
        return "user";
    }
}
