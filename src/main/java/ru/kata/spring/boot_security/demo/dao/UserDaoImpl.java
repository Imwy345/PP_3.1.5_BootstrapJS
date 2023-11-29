package ru.kata.spring.boot_security.demo.dao;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Collections;
import java.util.List;

@Repository
public class UserDaoImpl implements UserDao, UserDetailsService {

    @PersistenceContext
    private EntityManager entityManager;


    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserDaoImpl( BCryptPasswordEncoder bCryptPasswordEncoder) {

        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public List<User> getAllUsers() {
        return entityManager.createQuery("select u from User u", User.class).getResultList();
    }

    @Override
    public User getUserById(long id) {
        return entityManager.find(User.class,id);
    }

    @Override
    public void addUser(User user) {
        entityManager.persist(user);
    }

    @Override
    public void removeUser(long id) {
        entityManager.remove(getUserById(id));
    }

    @Override
    public void updateUser(User user) {
        entityManager.merge(user);

    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        TypedQuery<User> query = entityManager.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class);
        query.setParameter("username", username);
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
    @Override
    @Transactional
    public boolean saveUser(User user, String roleName) {
        TypedQuery<User> userQuery = entityManager.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class);
        userQuery.setParameter("username", user.getUsername());

        try {
            User userFromDB = userQuery.getSingleResult();
            // Если пользователь с таким именем уже существует, возвращаем false
            return false;
        } catch (NoResultException e) {
            // Если пользователя с таким именем нет, продолжаем сохранение
        }

        TypedQuery<Role> roleQuery = entityManager.createQuery("SELECT r FROM Role r WHERE r.name = :name", Role.class);
        roleQuery.setParameter("name", roleName);

        Role role;
        try {
            role = roleQuery.getSingleResult();
        } catch (NoResultException e) {
            // Если роли с таким именем нет, создаем новую
            role = new Role();
            role.setName(roleName);
            entityManager.persist(role);
        }

        user.setRoles(Collections.singleton(role));
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        entityManager.persist(user);
        return true;
    }

}