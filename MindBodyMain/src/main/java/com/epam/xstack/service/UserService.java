package com.epam.xstack.service;

import com.epam.xstack.model.Role;
import com.epam.xstack.model.User;
import lombok.NonNull;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public interface UserService {
    User create(@NonNull String firstname, @NonNull String lastname, @NonNull String password, @NonNull Role role);

    boolean activate(String username, boolean activate);

    User findByUsername(String searchUsername);

    Object getProfile(@NonNull String username);

    void changePassword(String username, String oldPassword, String newPassword);

   Role getRole(String username);
}
