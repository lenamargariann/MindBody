package com.epam.xstack.dao.impl;

import com.epam.xstack.model.Training;
import com.epam.xstack.model.dto.TrainerDTO;
import com.epam.xstack.model.dto.TrainingFilterDTO;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TrainingDaoImplTest {

    @Mock
    private SessionFactory sessionFactory;

    @Mock
    private Session session;

    @Mock
    private Transaction transaction;

    @Mock
    private Query<Training> query;

    @InjectMocks
    private TrainingDaoImpl trainingDao;

    @BeforeEach
    public void setUp() {
        when(sessionFactory.openSession()).thenReturn(session);
    }

    @Test
    public void testCreateSuccess() {
        Training training = new Training();
        when(session.beginTransaction()).thenReturn(transaction);

        doNothing().when(session).persist(training);

        Optional<Training> result = trainingDao.create(training);

        assertTrue(result.isPresent());
        assertEquals(training, result.get());
    }

    @Test
    public void testCreateFailure() {
        Training training = new Training();
        when(session.beginTransaction()).thenReturn(transaction);

        doThrow(HibernateException.class).when(session).persist(training);

        Optional<Training> result = trainingDao.create(training);

        assertFalse(result.isPresent());
    }

    @Test
    public void testListTrainingForTrainee() {
        String username = "testUsername";
        when(session.createQuery(TrainingDaoImpl.GET_TRAINEE_TRAININGS, Training.class)).thenReturn(query);
        List<Training> expectedTraining = List.of(new Training());
        when(query.setParameter("username", username)).thenReturn(query);
        when(query.getResultList()).thenReturn(expectedTraining);

        List<Training> result = trainingDao.list(false, new TrainingFilterDTO());

        assertEquals(expectedTraining, result);
    }

    @Test
    public void testListTrainingForTrainer() {
        String username = "testUsername";
        when(session.createQuery(TrainingDaoImpl.GET_TRAINER_TRAININGS, Training.class)).thenReturn(query);
        List<Training> expectedTraining = List.of(new Training());
        when(query.setParameter("username", username)).thenReturn(query);
        when(query.getResultList()).thenReturn(expectedTraining);

        List<Training> result = trainingDao.list(true,new TrainingFilterDTO());

        assertEquals(expectedTraining, result);
    }

    @Test
    public void testListTrainingFailure() {
        String username = "testUsername";
        when(session.createQuery(TrainingDaoImpl.GET_TRAINEE_TRAININGS, Training.class)).thenReturn(query);
        when(query.setParameter("username", username)).thenReturn(query);
        when(query.getResultList()).thenThrow(HibernateException.class);

        List<Training> result = trainingDao.list(true,new TrainingFilterDTO());

        assertTrue(result.isEmpty());
    }

    @Test
    public void testChangeTrainerSuccess() {
        Training training = new Training();
        when(session.beginTransaction()).thenReturn(transaction);

        when(session.merge(training)).thenReturn(training);

        Optional<Training> result = trainingDao.changeTrainer(training);

        assertTrue(result.isPresent());
        assertEquals(training, result.get());
    }

    @Test
    public void testChangeTrainerFailure() {
        Training training = new Training();
        when(session.beginTransaction()).thenReturn(transaction);

        when(session.merge(training)).thenThrow(HibernateException.class);

        Optional<Training> result = trainingDao.changeTrainer(training);

        assertFalse(result.isPresent());
    }
    @AfterEach
    public void tearDown() {
        verify(session).close();  // You might want to verify if the session is being closed in each method of UserDaoImpl
    }
}
