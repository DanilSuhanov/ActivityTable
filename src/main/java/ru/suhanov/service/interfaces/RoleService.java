package ru.suhanov.service.interfaces;

import ru.suhanov.model.Role;

import java.util.List;

public interface RoleService {
    void addNewRole(Role role);
    Role findRoleById(Long id);
    Role findRoleByAuthority(String authority);
    List<Role> findAll();
    void deleteById(Long id);
}
