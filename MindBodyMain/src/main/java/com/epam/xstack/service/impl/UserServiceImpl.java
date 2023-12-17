package com.epam.xstack.service.impl;

import com.epam.xstack.dao.impl.UserDaoImpl;
import com.epam.xstack.model.User;
import com.epam.xstack.service.UserService;
import jakarta.annotation.Nonnull;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserDaoImpl userDao;
    private final PasswordEncoder passwordEncoder;


    @Override
    public User create(@Nonnull String firstname, @Nonnull String lastname, @NonNull String password) {
        String username = generateUniqueUsername(firstname, lastname);
        return new User(firstname, lastname, username, passwordEncoder.encode(password));
    }

    private String generateUniqueUsername(String firstname, String lastname) {
        return userDao.getValidUsername(firstname.concat(".").concat(lastname).toLowerCase());
    }


    @Override
    public boolean activate(String username, boolean isActive) {
        findByUsername(username);
        return userDao.activate(username, isActive);
    }

    @Override
    public User findByUsername(String username) {
        return userDao.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    @Override
    public Object getProfile(@NonNull String username) {
        return userDao.getProfile(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Error getting profile."));

    }

    @Override
    public void changePassword(String username, String oldPassword, String newPassword) {
        userDao.findByUsername(username)
                .filter(user -> passwordEncoder.matches(oldPassword, user.getPassword()))
                .map(user -> userDao.updatePassword(username, passwordEncoder.encode(newPassword)))
                .filter(aBoolean -> aBoolean)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password change failed"));
    }

}

