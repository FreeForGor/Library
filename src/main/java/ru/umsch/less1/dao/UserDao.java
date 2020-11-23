package ru.umsch.less1.dao;

import ru.umsch.less1.model.User;

public interface UserDao {
    User addUser(User user);

    User getUserByName(String userName);

    void deleteAll();
}
