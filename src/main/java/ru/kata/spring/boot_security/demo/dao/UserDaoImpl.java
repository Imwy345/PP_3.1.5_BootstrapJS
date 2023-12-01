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
import java.util.Set;

@Repository
public class UserDaoImpl implements UserDao {

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
    @Transactional
    public boolean saveUser(User user) {
        TypedQuery<User> userQuery = entityManager.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class);
        userQuery.setParameter("username", user.getUsername());

        try {
            User userFromDB = userQuery.getSingleResult();
            // Если пользователь с таким именем уже существует, возвращаем false
            return false;
        } catch (NoResultException e) {
            // Если пользователя с таким именем нет, продолжаем сохранение
        }
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        entityManager.persist(user);
        return true;
    }

}