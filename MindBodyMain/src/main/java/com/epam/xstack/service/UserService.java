package com.epam.xstack.service;

import com.epam.xstack.model.User;
import lombok.NonNull;

public interface UserService {
    User create(@NonNull String firstname, @NonNull String lastname, @NonNull String password);

    boolean activate(String username, boolean activate);

    User findByUsername(String searchUsername);

    Object getProfile(@NonNull String username);

    void changePassword(String username, String oldPassword, String newPassword);

 }
