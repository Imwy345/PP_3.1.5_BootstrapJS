package ru.kata.spring.boot_security.demo.dao;

import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.Role;

import java.util.List;
import java.util.Set;

public interface RoleDao {

    Role findRoleByName(String roleName);
    Set<Role> validateRoles(List<String> roleNames);

    Role saveRole(Role role);
}
