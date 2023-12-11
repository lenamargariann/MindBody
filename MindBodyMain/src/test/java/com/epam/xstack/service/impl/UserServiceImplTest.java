package com.epam.xstack.service.impl;

import com.epam.xstack.dao.impl.UserDaoImpl;
import com.epam.xstack.model.User;
import lombok.NonNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserDaoImpl userDao;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreate() {
        @NonNull String firstname = "John";
        @NonNull String lastname = "Doe";
        String username = firstname.concat(".").concat(lastname).toLowerCase();

        when(userDao.getValidUsername(username)).thenReturn("john.doe123");

        User result = userService.create(firstname, lastname, "dbscdjndskjncA");

        assertNotNull(result);
        assertEquals(firstname, result.getFirstname());
        assertEquals(lastname, result.getLastname());
    }

    @Test
    public void testUpdatePassword() {
        String username = "john.doe123";
        String newPassword = "newPassword123";

        userService.updatePassword(username, newPassword);

        verify(userDao, times(1)).updatePassword(username, newPassword);
    }


    @Test
    public void testActivate_UserActivated() {
        String username = "john.doe123";

        when(userDao.activate(username, true)).thenReturn(true);

        userService.activate(username, true);

        verify(userDao, times(1)).activate(username, true);
    }

    @Test
    public void testActivate_UserDeactivated() {
        String username = "john.doe123";

        when(userDao.activate(username, false)).thenReturn(false);

        userService.activate(username, false);

        verify(userDao, times(1)).activate(username, false);
    }

    @Test
    public void testFindByUsername() {
        String username = "john.doe123";
        User user = new User();

        when(userDao.findByUsername(username)).thenReturn(Optional.of(user));

        Optional<User> result = userService.findByUsername(username);

        assertTrue(result.isPresent());
        assertEquals(user, result.get());
    }

    @Test
    public void testGetProfile() {
        String username = "john.doe123";
        Object profile = new Object();

        when(userDao.getProfile(username)).thenReturn(profile);

        Object result = userService.getProfile(username);

        assertEquals(profile, result);
    }

    @Test
    public void testGenerateRandomPassword() {
        String password = UserServiceImpl.generateRandomPassword();

        assertNotNull(password);
        assertEquals(10, password.length());

        // Check if generated password contains only valid characters
        assertTrue(password.chars().allMatch(ch -> "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789".indexOf(ch) >= 0));
    }
}
