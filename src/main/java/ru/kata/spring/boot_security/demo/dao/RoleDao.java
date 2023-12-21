package ru.kata.spring.boot_security.demo.dao;

import ru.kata.spring.boot_security.demo.model.Role;

import java.util.Set;

public interface RoleDao {

    Role findRoleByName(String roleName);

    Set<Role> checkRoles(Set<Role> roleNames);

    void saveRole(Role role);
}
