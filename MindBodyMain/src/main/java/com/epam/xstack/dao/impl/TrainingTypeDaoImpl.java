package com.epam.xstack.dao.impl;


import com.epam.xstack.dao.TrainingTypeDao;
import com.epam.xstack.model.TrainingType;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class TrainingTypeDaoImpl implements TrainingTypeDao {
    static final String FIND_BY_NAME = "SELECT t FROM training_type t WHERE t.trainingTypeName = :trainingTypeName";
    static final String LIST = "SELECT t FROM training_type t";
    private final SessionFactory sessionFactory;

    public Optional<TrainingType> findByName(@Nonnull String trainingTypeName) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(FIND_BY_NAME, TrainingType.class).setParameter("trainingTypeName", trainingTypeName).uniqueResultOptional();
        } catch (HibernateException e) {
            log.error(e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public List<TrainingType> list() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(LIST, TrainingType.class).getResultList();
        } catch (HibernateException e) {
            log.error(e.getMessage());
            return new ArrayList<>();
        }
    }
}
