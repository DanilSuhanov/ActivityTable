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

    @GetMapping("/")
    public String index() {
        return "index";
    }
}
