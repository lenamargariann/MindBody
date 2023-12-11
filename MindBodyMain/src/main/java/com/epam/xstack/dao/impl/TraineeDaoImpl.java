package com.epam.xstack.dao.impl;

import com.epam.xstack.dao.TraineeDao;
import com.epam.xstack.model.Trainee;
import jakarta.annotation.Nonnull;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.resource.transaction.spi.TransactionStatus;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class TraineeDaoImpl implements TraineeDao {
    static final String LIST_BY_USERNAME = "SELECT t FROM trainee t WHERE t.user.username = :username";

    private final SessionFactory sessionFactory;

    @Override
    public Optional<Trainee> create(@Nonnull Trainee trainee) {
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            session.persist(trainee);
            tx.commit();
            return Optional.of(trainee);
        } catch (HibernateException e) {
            if (tx != null && tx.getStatus() == TransactionStatus.ACTIVE) {
                tx.rollback();
            }
            log.error(e.getMessage());
            return Optional.empty();
        }
    }


    @Override
    public Optional<Trainee> findByUsername(@NonNull String username) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(LIST_BY_USERNAME, Trainee.class).setParameter("username", username).uniqueResultOptional();
        } catch (HibernateException e) {
            log.error(e.getMessage());
            return Optional.empty();
        }
    }


    @Override
    public Optional<Trainee> update(@NonNull Trainee trainee) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tr = session.beginTransaction();
            trainee = session.merge(trainee);
            tr.commit();
            return Optional.of(trainee);
        } catch (HibernateException e) {
            log.error(e.getMessage());
            return Optional.empty();
        }
    }


    @Override
    public boolean delete(@NonNull Trainee trainee) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tr = session.beginTransaction();
            session.remove(trainee);
            tr.commit();
            return true;
        } catch (HibernateException e) {
            log.error(e.getMessage());
            return false;
        }
    }

}
