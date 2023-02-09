package ru.suhanov.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.suhanov.model.Role;
import ru.suhanov.model.User;
import ru.suhanov.service.interfaces.RoleService;
import ru.suhanov.service.interfaces.UserService;

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
    public String index() {
        return "allUsers";
    }

    @GetMapping("/user")
    public String userIndex() {
        return "allUsers";
    }

    @PostMapping("/admin/new")
    private String saveUser(@RequestParam("username") String username, @RequestParam("password") String password, @RequestParam("email") String email, @RequestParam("role") List<String> stringRoles) {
        userService.addNewUser(buildUserRoles(new User(username, password, email), stringRoles));
        return "redirect:/admin";
    }

    private User buildUserRoles(User user, List<String> stringRoles) {
        List<Role> roles = new ArrayList<>();
        stringRoles.forEach(r -> roles.add(roleService.findRoleByAuthority(r)));
        user.setRoles(roles);
        return user;
    }

    @PostMapping("/admin/edit")
    private String editUserPost(@RequestParam("id") Long id, @RequestParam("name") String name, @RequestParam("password") String password, @RequestParam("email") String email, @RequestParam("role") List<String> stringRoles) {
        userService.editUser(buildUserRoles(new User(id, name, password, email), stringRoles));
        return "redirect:/admin";
    }

    @GetMapping("/admin/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        userService.deleteUserById(id);
        return "redirect:/admin";
    }
}
