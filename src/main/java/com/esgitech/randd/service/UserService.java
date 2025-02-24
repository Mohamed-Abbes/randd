package com.esgitech.randd.service;

import com.esgitech.randd.entities.User;

import java.util.List;

public interface UserService {
    User saveUse(User user);
    List<User> findAllUsers();

}
