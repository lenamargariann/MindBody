package com.epam.xstack.dao.impl;

import com.epam.xstack.dao.UserDao;
import com.epam.xstack.model.Trainee;
import com.epam.xstack.model.Trainer;
import com.epam.xstack.model.User;
import io.micrometer.common.lang.Nullable;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
@Repository
@RequiredArgsConstructor
public class UserDaoImpl implements UserDao {
    static final String UPDATE_PASSWORD = "UPDATE user_account u SET u.password = :password WHERE u.username = :username";
    static final String GET_BY_USERNAME = "SELECT u FROM user_account u WHERE u.username = :username";
    static final String ACTIVATE = "UPDATE user_account u SET isActive = :isActive WHERE u.username = :username";
    static final String GET_TRAINER = "SELECT t FROM trainer t WHERE t.user.username = :username";
    static final String GET_TRAINEE = "SELECT t FROM trainee t WHERE t.user.username = :username";
    static final String GET_USERNAME_NUM = "SELECT u FROM user_account u WHERE u.username LIKE :searchString";

    private final SessionFactory sessionFactory;

    @Override
    public boolean updatePassword(@NonNull String username, @NonNull String password) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            int affectedRows = session.createMutationQuery(UPDATE_PASSWORD).setParameter("username", username).setParameter("password", password).executeUpdate();
            transaction.commit();
            return affectedRows > 0;
        } catch (HibernateException e) {
            log.error(e.getMessage());
            return false;
        }
    }

    @Override
    public boolean checkPasswordMatching(@NonNull String username, @NonNull String password) {
        final Boolean[] matches = {false};
        try (Session session = sessionFactory.openSession()) {
            session.createQuery(GET_BY_USERNAME, User.class).setParameter("username", username).uniqueResultOptional()
                    .ifPresentOrElse(user -> matches[0] = user.getPassword().equals(password), () -> matches[0] = false);
            return matches[0];
        } catch (HibernateException e) {
            log.error(e.getMessage());
            return false;
        }
    }

    @Override
    public boolean activate(@NonNull String username, boolean isActive) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tr = session.beginTransaction();
            int rowsAffected = session.createMutationQuery(ACTIVATE).setParameter("isActive", isActive).setParameter("username", username).executeUpdate();
            tr.commit();
            return rowsAffected > 0;
        } catch (HibernateException e) {
            log.error(e.getMessage());
            return false;
        }
    }

    @Override
    public Optional<User> findByUsername(@NonNull String username) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(GET_BY_USERNAME, User.class).setParameter("username", username).uniqueResultOptional();
        } catch (HibernateException e) {
            log.error(e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    @Nullable
    public Object getProfile(@NonNull String username) {
        try (Session session = sessionFactory.openSession()) {
            var response = new AtomicReference<>(null);
            session.createQuery(GET_TRAINEE, Trainee.class).setParameter("username", username).uniqueResultOptional()
                    .ifPresentOrElse(response::set,
                            () -> session.createQuery(GET_TRAINER, Trainer.class).setParameter("username", username).uniqueResultOptional()
                                    .ifPresent(response::set));
            return response.get();
        } catch (HibernateException hibernateException) {
            log.error(hibernateException.getMessage());
            return null;
        }
    }

    @Override
    public String getValidUsername(String usernamePrototype) {
        String username = null;
        try (Session session = sessionFactory.openSession()) {
            List<User> users = session.createQuery(GET_USERNAME_NUM, User.class).setParameter("searchString", usernamePrototype + "%").getResultList();
            username = users.stream()
                    .map(user -> {
                        int num = 0;
                        try {
                            num = Integer.parseInt(user.getUsername().replace(usernamePrototype, ""));
                        } catch (NumberFormatException e) {
                            log.error(e.getMessage());
                        }
                        return num;
                    })
                    .max(Integer::compareTo)
                    .map(integer -> usernamePrototype.concat(Objects.toString(integer + 1))).orElse(usernamePrototype);
        } catch (HibernateException hibernateException) {
            log.error(hibernateException.getMessage());
        }
        return username;

    }


}
