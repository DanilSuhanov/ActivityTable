package ru.kata.spring.boot_security.demo;

import ru.kata.spring.boot_security.demo.model.Role;

import java.util.List;
import java.util.stream.Collectors;

public class Util {
    public static List<String> getAuthorise(List<Role> roles) {
        return roles.stream().map(Role::getAuthority)
                .map(s -> s.replace("ROLE_", ""))
                .collect(Collectors.toList());
    }
}
