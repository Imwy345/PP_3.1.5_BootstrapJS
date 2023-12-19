package ru.kata.spring.boot_security.demo.dao;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.model.User;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class UserDaoImpl implements UserDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<User> getAllUsers() {
        return entityManager.createQuery("select u from User u", User.class).getResultList();
    }

    @Override
    public User getUserById(long id) {
        return entityManager.find(User.class, id);
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
    public void updateUserChangePassword(User user) {
        entityManager.merge(user);

    }

    @Override
    public boolean saveUser(User user) {
        TypedQuery<User> userQuery = entityManager.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class);
        userQuery.setParameter("username", user.getUsername());
        System.out.println("ДАО Сохранение, должно быть зашифрованно: "+user.getPassword());
        try {
            User userFromDB = userQuery.getSingleResult();
            return false;
        } catch (NoResultException e) {
        }
        System.out.println("Сохранение прошло успешно");
        entityManager.persist(user);
        return true;
    }

    @Override
    public User findByUsername(String username) throws UsernameNotFoundException {
        TypedQuery<User> query = entityManager.createQuery("SELECT u FROM User u LEFT JOIN FETCH u.roles WHERE u.username = :username", User.class);
        query.setParameter("username", username);
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

}