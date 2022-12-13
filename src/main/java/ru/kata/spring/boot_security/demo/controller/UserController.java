package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.kata.spring.boot_security.demo.Util;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
public class UserController {

    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public UserController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("/admin")
    public String index(Model model, Principal principal) {
        List<User> users = userService.findAll();
        User admin = users.stream()
                .filter(user -> user.getUsername().equals(principal.getName()))
                .findFirst().orElse(null);
        model.addAttribute("users", users);
        model.addAttribute("admin", admin);
        model.addAttribute("roles", Util.getAuthorise(admin.getRoles()));
        return "allUsers";
    }

    @GetMapping("/admin/new")
    private String newUser(Model model, Principal principal) {
        User admin = userService.findUserByUsername(principal.getName());
        model.addAttribute("user", new User());
        model.addAttribute("admin", admin);
        model.addAttribute("roles", Util.getAuthorise(admin.getRoles()));
        return "new";
    }

    @PostMapping("/admin/new")
    private String saveUser(User user, @RequestParam("role") List<String> stringRoles) {
        userService.addNewUser(buildUserRoles(user, stringRoles));
        return "redirect:/admin";
    }

    private User buildUserRoles(User user, List<String> stringRoles) {
        List<Role> roles = new ArrayList<>();
        stringRoles.forEach(r -> roles.add(roleService.findRoleByAuthority(r)));
        user.setRoles(roles);
        return user;
    }

    @GetMapping("/admin/edit/{id}")
    private String editUser(@PathVariable("id") Long id, Model model) {
        model.addAttribute("user", userService.findUserById(id));
        return "edit";
    }

    @PostMapping("/admin/edit")
    private String editUserPost(User user, @RequestParam("role") List<String> stringRoles) {
        userService.editUser(buildUserRoles(user, stringRoles));
        return "redirect:/admin";
    }

    @GetMapping("/admin/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        userService.deleteUserById(id);
        return "redirect:/admin";
    }

    @GetMapping("/user")
    private String profile(Principal principal, Model model) {
        User user = userService.findUserByUsername(principal.getName());
        model.addAttribute("user", user);
        model.addAttribute("roles", Util.getAuthorise(user.getRoles()));
        return "user";
    }
}
