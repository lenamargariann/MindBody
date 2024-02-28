package com.epam.xstack.dao.impl;


import com.epam.xstack.model.Trainee;
import com.epam.xstack.model.Trainer;
import com.epam.xstack.model.User;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserDaoImplTest {

    @Mock
    private SessionFactory sessionFactory;

    @Mock
    private Session session;

    @Mock
    private Query<User> userQuery;

    @Mock
    private Query<Long> longQuery;

    @Mock
    private Query<Trainee> traineeQuery;

    @Mock
    private Query<Trainer> trainerQuery;

    @Mock
    private Transaction transaction;

    @InjectMocks
    private UserDaoImpl userDao;

    private static final String TEST_USERNAME = "testUsername";
    private static final String TEST_PASSWORD = "testPassword";

    @BeforeEach
    public void setUp() {
        when(sessionFactory.openSession()).thenReturn(session);
    }

    @Test
    public void testUpdatePasswordSuccess() {
        when(session.beginTransaction()).thenReturn(transaction);

        when(session.createMutationQuery(UserDaoImpl.UPDATE_PASSWORD)).thenReturn(userQuery);
        when(userQuery.setParameter(anyString(), any())).thenReturn(userQuery);
        when(userQuery.executeUpdate()).thenReturn(1);

        boolean result = userDao.updatePassword(TEST_USERNAME, TEST_PASSWORD);

        assertTrue(result);
    }

    @Test
    public void testUpdatePasswordFailure() {
        when(session.beginTransaction()).thenReturn(transaction);

        when(session.createMutationQuery(UserDaoImpl.UPDATE_PASSWORD)).thenReturn(userQuery);
        when(userQuery.setParameter(anyString(), any())).thenReturn(userQuery);
        when(userQuery.executeUpdate()).thenThrow(HibernateException.class);

        boolean result = userDao.updatePassword(TEST_USERNAME, TEST_PASSWORD);

        assertFalse(result);
    }


    @Test
    public void testFindByUsernameSuccess() {
        User user = new User();
        when(session.createQuery(UserDaoImpl.GET_BY_USERNAME, User.class)).thenReturn(userQuery);
        when(userQuery.setParameter("username", TEST_USERNAME)).thenReturn(userQuery);
        when(userQuery.uniqueResultOptional()).thenReturn(Optional.of(user));

        Optional<User> result = userDao.findByUsername(TEST_USERNAME);

        assertTrue(result.isPresent());
        assertEquals(user, result.get());
    }

    @Test
    public void testCheckPasswordMatchingSuccess() {
        User user = new User();
        user.setPassword(TEST_PASSWORD);

        when(session.createQuery(UserDaoImpl.GET_BY_USERNAME, User.class)).thenReturn(userQuery);
        when(userQuery.setParameter("username", TEST_USERNAME)).thenReturn(userQuery);
        when(userQuery.uniqueResultOptional()).thenReturn(Optional.of(user));

        boolean result = userDao.checkPasswordMatching(TEST_USERNAME, TEST_PASSWORD);

        assertTrue(result);
    }

    @Test
    public void testCheckPasswordMatchingFailureNoUser() {
        when(session.createQuery(UserDaoImpl.GET_BY_USERNAME, User.class)).thenReturn(userQuery);
        when(userQuery.setParameter("username", TEST_USERNAME)).thenReturn(userQuery);
        when(userQuery.uniqueResultOptional()).thenReturn(Optional.empty());

        boolean result = userDao.checkPasswordMatching(TEST_USERNAME, TEST_PASSWORD);

        assertFalse(result);
    }

    @Test
    public void testActivateSuccess() {
        when(session.beginTransaction()).thenReturn(transaction);

        when(session.createMutationQuery(UserDaoImpl.ACTIVATE)).thenReturn(userQuery);
        when(userQuery.setParameter(anyString(), any())).thenReturn(userQuery);
        when(userQuery.executeUpdate()).thenReturn(1);

        boolean result = userDao.activate(TEST_USERNAME, true);

        assertTrue(result);
    }

    @Test
    public void testActivateFailure() {
        when(session.beginTransaction()).thenReturn(transaction);

        when(session.createMutationQuery(UserDaoImpl.ACTIVATE)).thenReturn(userQuery);
        when(userQuery.setParameter(anyString(), any())).thenReturn(userQuery);
        when(userQuery.executeUpdate()).thenReturn(0);

        boolean result = userDao.activate(TEST_USERNAME, true);

        assertFalse(result);
    }

//    @Test
//    public void testGetProfileTrainee() {
//        Trainee trainee = new Trainee();
//
//        when(session.createQuery(UserDaoImpl.GET_TRAINEE, Trainee.class)).thenReturn(traineeQuery);
//        when(traineeQuery.setParameter("username", TEST_USERNAME)).thenReturn(traineeQuery);
//        when(traineeQuery.uniqueResultOptional()).thenReturn(Optional.of(trainee));
//
//        Object result = userDao.getProfile(TEST_USERNAME);
//
//        assertNotNull(result);
//        assertTrue(result instanceof Trainee);
//    }

//    @Test
//    public void testGetProfileTrainer() {
//        Trainer trainer = new Trainer();
//
//        when(session.createQuery(UserDaoImpl.GET_TRAINEE, Trainee.class)).thenReturn(traineeQuery);
//        when(traineeQuery.setParameter("username", TEST_USERNAME)).thenReturn(traineeQuery);
//        when(traineeQuery.uniqueResultOptional()).thenReturn(Optional.empty());
//
//        when(session.createQuery(UserDaoImpl.GET_TRAINER, Trainer.class)).thenReturn(trainerQuery);
//        when(trainerQuery.setParameter("username", TEST_USERNAME)).thenReturn(trainerQuery);
//        when(trainerQuery.uniqueResultOptional()).thenReturn(Optional.of(trainer));
//
//        Object result = userDao.getProfile(TEST_USERNAME);
//
//        assertNotNull(result);
//        assertTrue(result instanceof Trainer);
//    }

//    @Test
//    public void testGetUsernameIdExisting() {
//        long count = 1;
//
//        when(session.createQuery(UserDaoImpl.GET_USERNAME_NUM, Long.class)).thenReturn(longQuery);
//        when(longQuery.setParameter("searchString", TEST_USERNAME + "%")).thenReturn(longQuery);
//        when(longQuery.uniqueResultOptional()).thenReturn(Optional.of(count));
//
//        String result = userDao.getValidUsername(TEST_USERNAME);
//
//        assertEquals(TEST_USERNAME.concat(String.valueOf(count + 1)), result);
//    }

//    @Test
//    public void testGetUsernameIdNotExisting() {
//        when(session.createQuery(UserDaoImpl.GET_USERNAME_NUM, Long.class)).thenReturn(longQuery);
//        when(longQuery.setParameter("searchString", TEST_USERNAME + "%")).thenReturn(longQuery);
//        when(longQuery.uniqueResultOptional()).thenReturn(Optional.empty());
//
//        String result = userDao.getValidUsername(TEST_USERNAME);
//
//        assertEquals(TEST_USERNAME, result);
//    }

    @Test
    public void testCheckPasswordMatchingFailureIncorrectPassword() {
        User user = new User();
        user.setPassword("differentPassword");

        when(session.createQuery(UserDaoImpl.GET_BY_USERNAME, User.class)).thenReturn(userQuery);
        when(userQuery.setParameter("username", TEST_USERNAME)).thenReturn(userQuery);
        when(userQuery.uniqueResultOptional()).thenReturn(Optional.of(user));

        boolean result = userDao.checkPasswordMatching(TEST_USERNAME, TEST_PASSWORD);

        assertFalse(result);
    }


//    @Test
//    public void testGetProfileFailure() {
//        when(session.createQuery(UserDaoImpl.GET_TRAINEE, Trainee.class)).thenReturn(traineeQuery);
//        when(traineeQuery.setParameter("username", TEST_USERNAME)).thenReturn(traineeQuery);
//        when(traineeQuery.uniqueResultOptional()).thenReturn(Optional.empty());
//
//        when(session.createQuery(UserDaoImpl.GET_TRAINER, Trainer.class)).thenReturn(trainerQuery);
//        when(trainerQuery.setParameter("username", TEST_USERNAME)).thenReturn(trainerQuery);
//        when(trainerQuery.uniqueResultOptional()).thenReturn(Optional.empty());
//
//        Object result = userDao.getProfile(TEST_USERNAME);
//
//        assertNull(result);
//    }

    @Test
    public void testHibernateExceptionUpdatePassword() {
        when(session.createMutationQuery(UserDaoImpl.UPDATE_PASSWORD)).thenThrow(HibernateException.class);

        boolean result = userDao.updatePassword(TEST_USERNAME, TEST_PASSWORD);

        assertFalse(result);
    }

    @Test
    public void testHibernateExceptionFindByUsername() {
        when(session.createQuery(UserDaoImpl.GET_BY_USERNAME, User.class)).thenThrow(HibernateException.class);

        Optional<User> result = userDao.findByUsername(TEST_USERNAME);

        assertFalse(result.isPresent());
    }

//    @Test
//    public void testHibernateExceptionGetUsernameId() {
//        when(session.createQuery(UserDaoImpl.GET_USERNAME_NUM, Long.class)).thenThrow(HibernateException.class);
//
//        String result = userDao.getValidUsername(TEST_USERNAME);
//
//        assertEquals(TEST_USERNAME, result);  // It should fall back to the input value in case of an exception
//    }

    @AfterEach
    public void tearDown() {
        verify(session).close();  // You might want to verify if the session is being closed in each method of UserDaoImpl
    }
}
