package ru.kata.spring.boot_security.demo.dao;

import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.model.Role;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
public class RoleDaoImpl implements RoleDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Role> getAllRoles() {
        TypedQuery<Role> query = entityManager.createQuery("SELECT r FROM Role r", Role.class);
        return query.getResultList();
    }

    @Override
    public Role findRoleByName(String roleName) {
        TypedQuery<Role> roleQuery = entityManager.createQuery("SELECT r FROM Role r WHERE r.name = :name", Role.class);
        roleQuery.setParameter("name", roleName);

        try {
            return roleQuery.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public Set<Role> validateRoles(List<String> roleNames) {
        Set<Role> roles = new HashSet<>();
        for (String roleName : roleNames) {
            Role role = findRoleByName(roleName);
            if (role != null) {
                roles.add(role);
            }
        }
        return roles;
    }

    @Override
    public void saveRole(Role role) {
        entityManager.persist(role);
    }

}
