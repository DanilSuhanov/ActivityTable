package ru.suhanov;

import ru.suhanov.model.Role;
import ru.suhanov.model.Task;
import ru.suhanov.model.User;

import java.util.List;
import java.util.stream.Collectors;

public class Util {
    public static List<String> getAuthorise(List<Role> roles) {
        return roles.stream().map(Role::getAuthority)
                .map(s -> s.replace("ROLE_", ""))
                .collect(Collectors.toList());
    }
}
