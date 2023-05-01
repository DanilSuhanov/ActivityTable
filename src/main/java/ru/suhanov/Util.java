package ru.suhanov;

import ru.suhanov.model.Role;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class Util {
    public static List<String> getAuthorise(List<Role> roles) {
        return roles.stream().map(Role::getAuthority)
                .map(s -> s.replace("ROLE_", ""))
                .collect(Collectors.toList());
    }

    public static String dateToString(LocalDateTime localDateTime) {
        return additionNull(localDateTime.getHour()) +
                ":" + additionNull(localDateTime.getMinute()) +
                " " + additionNull(localDateTime.getDayOfMonth()) +
                "." + additionNull(localDateTime.getMonthValue()) +
                "." + localDateTime.getYear();
    }

    private static String additionNull(int x) {
        if (x < 10) {
            return "0" + x;
        } else {
            return String.valueOf(x);
        }
    }
}