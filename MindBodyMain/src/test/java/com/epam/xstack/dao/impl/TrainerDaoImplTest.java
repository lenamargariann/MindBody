package com.epam.xstack.dao.impl;

import com.epam.xstack.model.Trainer;
import com.epam.xstack.model.TrainingType;
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

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TrainerDaoImplTest {

    @Mock
    private SessionFactory sessionFactory;

    @Mock
    private Session session;

    @Mock
    private Transaction transaction;

    @Mock
    private Query<Trainer> query;

    @InjectMocks
    private TrainerDaoImpl trainerDao;

    @BeforeEach
    public void setUp() {
        when(sessionFactory.openSession()).thenReturn(session);
    }

    @Test
    public void testCreateSuccess() {
        Trainer trainer = new Trainer();
        when(session.beginTransaction()).thenReturn(transaction);

        doNothing().when(session).persist(trainer);

        Optional<Trainer> result = trainerDao.create(trainer);

        assertTrue(result.isPresent());
        assertEquals(trainer, result.get());
    }

    @Test
    public void testCreateFailure() {
        Trainer trainer = new Trainer();
        when(session.beginTransaction()).thenReturn(transaction);

        doThrow(HibernateException.class).when(session).persist(trainer);

        Optional<Trainer> result = trainerDao.create(trainer);

        assertFalse(result.isPresent());
    }

//    @Test
//    public void testFindByUsername() {
//        String username = "testUsername";
//        when(session.createQuery(TrainerDaoImpl.LIST_BY_USERNAME, Trainer.class)).thenReturn(query);
//        List<Trainer> expectedTrainers = List.of(new Trainer());
//        when(query.setParameter("username", username)).thenReturn(query);
//        when(query.getResultList()).thenReturn(expectedTrainers);
//
//        List<Trainer> result = trainerDao.findByUsername(username);
//
//        assertEquals(expectedTrainers, result);
//    }

    @Test
    public void testUpdateSuccess() {
        Trainer trainer = new Trainer();
        when(session.beginTransaction()).thenReturn(transaction);

        when(session.merge(trainer)).thenReturn(trainer);

        Optional<Trainer> result = trainerDao.update(trainer);

        assertTrue(result.isPresent());
        assertEquals(trainer, result.get());
    }

    @Test
    public void testUpdateFailure() {
        Trainer trainer = new Trainer();
        when(session.beginTransaction()).thenReturn(transaction);

        when(session.merge(trainer)).thenThrow(HibernateException.class);

        Optional<Trainer> result = trainerDao.update(trainer);

        assertFalse(result.isPresent());
    }

    @Test
    public void testListNotAssigned() {
        when(session.createQuery(TrainerDaoImpl.LIST_NOT_ASSIGNED, Trainer.class)).thenReturn(query);
        List<Trainer> expectedTrainers = List.of(new Trainer());
        when(query.getResultList()).thenReturn(expectedTrainers);

        List<Trainer> result = trainerDao.listNotAssigned();

        assertEquals(expectedTrainers, result);
    }

    @Test
    public void testListBySpecialization() {
        TrainingType trainingType = new TrainingType();
        trainingType.setId(1L);
        when(session.createQuery(TrainerDaoImpl.TRAINERS_LIST_BY_TRAINING_TYPE, Trainer.class)).thenReturn(query);
        List<Trainer> expectedTrainers = List.of(new Trainer());
        when(query.setParameter("trainingTypeId", trainingType.getId())).thenReturn(query);
        when(query.getResultList()).thenReturn(expectedTrainers);

        List<Trainer> result = trainerDao.listBySpecialization(trainingType);

        assertEquals(expectedTrainers, result);
    }
    @AfterEach
    public void tearDown() {
        verify(session).close();  // You might want to verify if the session is being closed in each method of UserDaoImpl
    }
}
