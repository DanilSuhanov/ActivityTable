package ru.kata.spring.boot_security.demo.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.HashSet;
import java.util.UUID;

@Service("userDetailsService")
public class CustomUserDetailsService implements UserDetailsService {

    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public CustomUserDetailsService(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;

        preInit();
    }

    private static String generateString() {
        return UUID.randomUUID().toString();
    }

    private void preInit() {
        Role adminRole = null;
        Role userRole = null;

        String authorityAdmin = "ROLE_ADMIN";
        String authorityUser = "ROLE_USER";

        if (roleService.findRoleByAuthority(authorityAdmin) == null) {
            adminRole = new Role();
            adminRole.setAuthority(authorityAdmin);
            adminRole.setUsers(new HashSet<>());

            roleService.addNewRole(adminRole);
        }

        if (roleService.findRoleByAuthority(authorityUser) == null) {
            userRole = new Role();
            userRole.setAuthority(authorityUser);
            userRole.setUsers(new HashSet<>());

            roleService.addNewRole(userRole);
        }

        adminRole = roleService.findRoleByAuthority(authorityAdmin);
        userRole = roleService.findRoleByAuthority(authorityUser);

        if (userService.findUserByUsername("admin") == null) {
            User user = new User();
            user.setUsername("admin");
            user.setEmail("email");
            user.setPassword(generateString());
            user.setRoles(new HashSet<>());

            user.addRole(adminRole);
            user.addRole(userRole);

            userService.addNewUser(user);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userService.findUserByUsername(username);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
}
