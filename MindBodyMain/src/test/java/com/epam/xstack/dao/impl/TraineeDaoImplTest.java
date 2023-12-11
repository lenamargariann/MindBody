package com.epam.xstack.dao.impl;

import com.epam.xstack.model.Trainee;
import lombok.extern.slf4j.Slf4j;
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

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.epam.xstack.dao.impl.TraineeDaoImpl.LIST_BY_USERNAME;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class TraineeDaoImplTest {

    @Mock
    private SessionFactory sessionFactory;

    @Mock
    private Session session;
    @Mock
    private Query<Trainee> query;
    @Mock
    private Transaction transaction;

    @InjectMocks
    private TraineeDaoImpl traineeDao;

    @BeforeEach
    public void setUp() {
        when(sessionFactory.openSession()).thenReturn(session);
    }


    @Test
    public void testCreateSuccess() {
        Trainee trainee = new Trainee();
        when(session.beginTransaction()).thenReturn(transaction);

        doNothing().when(session).persist(trainee);

        Optional<Trainee> result = traineeDao.create(trainee);

        assertTrue(result.isPresent());
        assertEquals(trainee, result.get());
    }

    @Test
    public void testCreateFailure() {
        Trainee trainee = new Trainee();
        when(session.beginTransaction()).thenReturn(transaction);

        doThrow(HibernateException.class).when(session).persist(trainee);

        Optional<Trainee> result = traineeDao.create(trainee);

        assertFalse(result.isPresent());
    }

//    @Test
//    public void testFindByUsernameSuccess() {
//        String username = "testUsername";
//        when(session.createQuery(LIST_BY_USERNAME, Trainee.class)).thenReturn(query);
//        List<Trainee> expectedTrainees = Arrays.asList(new Trainee(), new Trainee());
//        when(query.setParameter("username", username)).thenReturn(query);
//        when(query.getResultList()).thenReturn(expectedTrainees);
//
//        List<Trainee> resultTrainees = traineeDao.findByUsername(username);
//
//        assertEquals(expectedTrainees, resultTrainees);
//    }

//    @Test
//    public void testFindByUsernameWithHibernateException() {
//        String username = "testUsername";
//        when(session.createQuery(LIST_BY_USERNAME, Trainee.class)).thenReturn(query);
//        when(query.setParameter("username", username)).thenReturn(query);
//        when(query.getResultList()).thenThrow(HibernateException.class);
//
//        List<Trainee> resultTrainees = traineeDao.findByUsername(username);
//
//        assertTrue(resultTrainees.isEmpty());
//    }

    @Test
    public void testUpdateSuccess() {
        Trainee trainee = new Trainee();
        when(session.beginTransaction()).thenReturn(transaction);

        when(session.merge(trainee)).thenReturn(trainee);

        Optional<Trainee> result = traineeDao.update(trainee);

        assertTrue(result.isPresent());
        assertEquals(trainee, result.get());
    }

    @Test
    public void testUpdateFailure() {
        Trainee trainee = new Trainee();
        when(session.beginTransaction()).thenReturn(transaction);

        when(session.merge(trainee)).thenThrow(HibernateException.class);

        Optional<Trainee> result = traineeDao.update(trainee);

        assertFalse(result.isPresent());
    }

    @Test
    public void testDeleteSuccess() {
        Trainee trainee = new Trainee();
        when(session.beginTransaction()).thenReturn(transaction);

        boolean result = traineeDao.delete(trainee);

        assertTrue(result);
    }

    @Test
    public void testDeleteFailure() {
        Trainee trainee = new Trainee();
        when(session.beginTransaction()).thenReturn(transaction);

        doThrow(HibernateException.class).when(session).remove(trainee);

        boolean result = traineeDao.delete(trainee);

        assertFalse(result);
    }

    @AfterEach
    public void tearDown() {
        verify(session).close();  // You might want to verify if the session is being closed in each method of UserDaoImpl
    }
}