package ru.umsch.less1.dao;

import org.springframework.stereotype.Repository;
import ru.umsch.less1.model.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;


@Repository
public class UserDaoImpl implements UserDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public User addUser(User user) {
        em.persist(user);
        return user;
    }

    @Override
    public User getUserByName(String userName) {
        TypedQuery<User> query = em.createQuery("SELECT u FROM User u WHERE u.userName = :userName", User.class);
        query.setParameter("userName", userName);
        try {
            return query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void deleteAll() {
        em.createQuery("DELETE FROM User").executeUpdate();
    }
}