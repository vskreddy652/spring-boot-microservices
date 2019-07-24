package com.eg.service;

import java.util.List;

import com.eg.model.User;

public interface UserService {

    User save(User user);
    List<User> findAll();
    void delete(long id);
}
