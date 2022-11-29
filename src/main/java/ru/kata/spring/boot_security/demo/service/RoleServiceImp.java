package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.repositoty.RoleRepository;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class RoleServiceImp implements RoleService {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleServiceImp(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    @Transactional
    public void addNewRole(Role role) {
        if (roleRepository.findByAuthority(role.getAuthority()) == null) {
            roleRepository.save(role);
        }
    }

    @Override
    @Transactional
    public Role findRoleById(Long id) {
        return roleRepository.findById(id).orElse(null);
    }

    @Override
    public Role findRoleByAuthority(String authority) {
        return roleRepository.findByAuthority(authority);
    }

    @Override
    @Transactional
    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        roleRepository.deleteById(id);
    }
}
