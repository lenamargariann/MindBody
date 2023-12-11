package com.epam.xstack.dao.impl;


import com.epam.xstack.dao.TrainerDao;
import com.epam.xstack.model.Trainer;
import com.epam.xstack.model.TrainingType;
import jakarta.transaction.Transactional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class TrainerDaoImpl implements TrainerDao {
    static final String LIST_BY_USERNAME = "SELECT t FROM trainer t WHERE t.user.username = :username";
    static final String LIST_NOT_ASSIGNED = "SELECT t FROM trainer t WHERE t.user.isActive=true and NOT EXISTS (SELECT tr FROM trainee tr JOIN tr.trainers tt WHERE tt = t)";
    static final String TRAINERS_LIST_BY_TRAINING_TYPE = "SELECT t FROM trainer t WHERE t.trainingType.id = :trainingTypeId";

    private final SessionFactory sessionFactory;

    @Override
    public Optional<Trainer> create(@NonNull Trainer trainer) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tr = session.beginTransaction();
            session.persist(trainer);
            tr.commit();
            return Optional.of(trainer);
        } catch (HibernateException e) {
            log.error(e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public Optional<Trainer> findByUsername(@NonNull String username) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(LIST_BY_USERNAME, Trainer.class).setParameter("username", username).uniqueResultOptional();
        } catch (HibernateException e) {
            log.error(e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    @Transactional
    public Optional<Trainer> update(@NonNull Trainer trainer) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tr = session.beginTransaction();
            trainer = session.merge(trainer);
            tr.commit();
            return Optional.of(trainer);
        } catch (HibernateException e) {
            log.error(e.getMessage());
            return Optional.empty();
        }
    }


    @Override
    public List<Trainer> listNotAssigned() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(LIST_NOT_ASSIGNED, Trainer.class).getResultList();
        } catch (HibernateException e) {
            log.error(e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public List<Trainer> listBySpecialization(TrainingType trainingType) {
        List<Trainer> trainers = new ArrayList<>();
        try (Session session = sessionFactory.openSession()) {
            trainers = session.createQuery(TRAINERS_LIST_BY_TRAINING_TYPE, Trainer.class).setParameter("trainingTypeId", trainingType.getId()).getResultList();
        } catch (HibernateException e) {
            log.error(e.getMessage());
        }
        return trainers;
    }
}
