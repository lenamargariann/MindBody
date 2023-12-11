package com.epam.xstack.service.impl;

import com.epam.xstack.dao.impl.UserDaoImpl;
import com.epam.xstack.model.User;
import com.epam.xstack.model.dto.UserDTO;
import com.epam.xstack.service.UserService;
import jakarta.annotation.Nonnull;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private static final String CHAR_POOL = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final SecureRandom random = new SecureRandom();
    private final UserDaoImpl userDao;
    private final PasswordEncoder passwordEncoder;


    public static String generateRandomPassword() {
        int PASSWORD_LENGTH = 10;
        StringBuilder password = new StringBuilder(PASSWORD_LENGTH);
        for (int i = 0; i < PASSWORD_LENGTH; i++) {
            int randomIndex = random.nextInt(CHAR_POOL.length());
            password.append(CHAR_POOL.charAt(randomIndex));
        }
        return password.toString();
    }

    @Override
    public User create(@Nonnull String firstname, @Nonnull String lastname, @NonNull String password) {
        String username = generateUniqueUsername(firstname, lastname);
        return new User(firstname, lastname, username, passwordEncoder.encode(password));
    }

    private String generateUniqueUsername(String firstname, String lastname) {
        return userDao.getValidUsername(firstname.concat(".").concat(lastname).toLowerCase());
    }

    @Override
    public void updatePassword(String username, String newPassword) {
        if (newPassword == null || newPassword.isEmpty()) {
            log.error("New password is null or empty");
            return;
        }
        userDao.updatePassword(username, passwordEncoder.encode(newPassword));
        log.debug("Password successfully updated for username: {}", username);
    }


    @Override
    public boolean activate(String username, boolean isActive) {
        try {
            return userDao.activate(username, isActive);
        } catch (Exception e) {
            log.error("Error activating user: {}", e.getMessage());
            return false;
        }
    }

    @Override
    public Optional<User> findByUsername(String username) {
        try {
            return userDao.findByUsername(username);
        } catch (Exception e) {
            log.error("Error finding user: {}", e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public Object getProfile(@NonNull String username) {
        try {
            return userDao.getProfile(username);
        } catch (Exception e) {
            log.error("Error getting profile for user: {}", e.getMessage());
            return null;
        }
    }

    @Override
    public Optional<User> login(UserDTO userDTO) {
        return findByUsername(userDTO.getUsername()).filter(user -> checkPassword(userDTO.getPassword(), user.getPassword()));
    }

    private boolean checkPassword(String plainText, String hashedPassword) {
        return passwordEncoder.encode(plainText).equals(hashedPassword);
    }

    @Override
    public boolean changePassword(String username, String oldPassword, String newPassword) {
        return findByUsername(username).filter(user -> checkPassword(oldPassword, user.getPassword()))
                .map(user -> userDao.updatePassword(username, passwordEncoder.encode(newPassword)))
                .orElse(false);
    }

    @Override
    public boolean logout() {
        return false;
    }
}

