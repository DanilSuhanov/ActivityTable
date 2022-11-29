package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.model.Role;

import java.util.List;

public interface RoleService {
    void addNewRole(Role role);
    Role findRoleById(Long id);
    Role findRoleByAuthority(String authority);
    List<Role> findAll();
    void deleteById(Long id);
}
