package com.epam.xstack.dao.impl;

import com.epam.xstack.model.TrainingType;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TrainingTypeDaoImplTest {

    @Mock
    private SessionFactory sessionFactory;

    @Mock
    private Session session;

    @Mock
    private Query<TrainingType> query;

    @InjectMocks
    private TrainingTypeDaoImpl trainingTypeDao;

    @BeforeEach
    public void setUp() {
        when(sessionFactory.openSession()).thenReturn(session);
    }

    @Test
    public void testFindByNameSuccess() {
        String trainingTypeName = "testTrainingType";
        TrainingType trainingType = new TrainingType();
        when(session.createQuery(TrainingTypeDaoImpl.FIND_BY_NAME, TrainingType.class)).thenReturn(query);
        when(query.setParameter("trainingTypeName", trainingTypeName)).thenReturn(query);
        when(query.uniqueResultOptional()).thenReturn(Optional.of(trainingType));

        Optional<TrainingType> result = trainingTypeDao.findByName(trainingTypeName);

        assertTrue(result.isPresent());
        assertEquals(trainingType, result.get());
    }

    @Test
    public void testFindByNameNotFound() {
        String trainingTypeName = "testTrainingType";
        when(session.createQuery(TrainingTypeDaoImpl.FIND_BY_NAME, TrainingType.class)).thenReturn(query);
        when(query.setParameter("trainingTypeName", trainingTypeName)).thenReturn(query);
        when(query.uniqueResultOptional()).thenReturn(Optional.empty());

        Optional<TrainingType> result = trainingTypeDao.findByName(trainingTypeName);

        assertFalse(result.isPresent());
    }

    @Test
    public void testFindByNameFailure() {
        String trainingTypeName = "testTrainingType";
        when(session.createQuery(TrainingTypeDaoImpl.FIND_BY_NAME, TrainingType.class)).thenReturn(query);
        when(query.setParameter("trainingTypeName", trainingTypeName)).thenReturn(query);
        when(query.uniqueResultOptional()).thenThrow(HibernateException.class);

        Optional<TrainingType> result = trainingTypeDao.findByName(trainingTypeName);

        assertFalse(result.isPresent());
    }
    @AfterEach
    public void tearDown() {
        verify(session).close();  // You might want to verify if the session is being closed in each method of UserDaoImpl
    }
}
