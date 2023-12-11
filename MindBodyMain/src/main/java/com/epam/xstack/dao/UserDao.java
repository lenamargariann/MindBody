package com.epam.xstack.dao;

import com.epam.xstack.model.User;
import lombok.NonNull;

import java.util.Optional;

public interface UserDao {
    boolean updatePassword(@NonNull String username, @NonNull String password);

    boolean checkPasswordMatching(@NonNull String username, @NonNull String password);

    boolean activate(String username, boolean isActive);

    Optional<User> findByUsername(String username);

    Object getProfile(String username);

    String getValidUsername(String usernamePrototype);
}
