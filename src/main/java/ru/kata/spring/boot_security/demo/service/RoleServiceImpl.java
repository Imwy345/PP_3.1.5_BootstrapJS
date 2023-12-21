package ru.kata.spring.boot_security.demo.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.dao.RoleDao;
import ru.kata.spring.boot_security.demo.model.Role;

import java.util.List;
import java.util.Set;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleDao roleDao;

    public RoleServiceImpl(RoleDao roleDao) {
        this.roleDao = roleDao;
    }


    @Override
    public Role findRoleByName(String roleName) {
        return roleDao.findRoleByName(roleName);
    }

@Override
    public Set<Role> checkRoles(Set<Role> roleNames){
        return roleDao.checkRoles(roleNames);
    }
    @Override
    @Transactional
    public void saveRole(Role role) {
        roleDao.saveRole(role);
    }
}
