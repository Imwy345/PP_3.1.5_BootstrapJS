package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.model.Role;

import java.util.List;
import java.util.Set;

public interface RoleService {

    List<Role> getAllRoles();

    Role findRoleByName(String roleName);
    Set<Role> proverkaRoles(Set<Role> roleNames);
    Set<Role> validateRoles(Set<String> roleNames);

    void saveRole(Role role);
}
