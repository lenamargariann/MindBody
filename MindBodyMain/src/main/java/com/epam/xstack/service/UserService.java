package com.epam.xstack.service;

import com.epam.xstack.model.User;
import com.epam.xstack.model.dto.UserDTO;
import lombok.NonNull;

import java.util.Optional;

public interface UserService {
    User create(@NonNull String firstname, @NonNull String lastname, @NonNull String password);

    void updatePassword(String username, String newPassword);

    boolean activate(String username, boolean activate);

    Optional<User> findByUsername(String searchUsername);

    Object getProfile(@NonNull String username);

    Optional<User> login(UserDTO userDTO);

    boolean changePassword(String username, String oldPassword, String newPassword);

    boolean logout();
}
