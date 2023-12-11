package com.epam.xstack.dao.impl;

import com.epam.xstack.dao.TrainingDao;
import com.epam.xstack.model.Training;
import com.epam.xstack.model.dto.RequestTrainingDTO;
import com.epam.xstack.model.dto.TrainingFilterDTO;
import jakarta.annotation.Nonnull;
import jakarta.persistence.criteria.Predicate;
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
public class TrainingDaoImpl implements TrainingDao {
    static final String GET_TRAINEE_TRAININGS = "FROM training t where trainee.user.username = :username";
    static final String GET_TRAINER_TRAININGS = "FROM training t where trainer.user.username = :username";
    private static final String GET_TRAINING = "FROM training t WHERE t.trainee.user.username = :traineeName AND t.trainer.user.username = :trainerName AND t.date = :date AND t.duration = :duration";

    private final SessionFactory sessionFactory;

    @Override
    public Optional<Training> create(@Nonnull Training training) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.persist(training);
            transaction.commit();
            return Optional.of(training);
        } catch (HibernateException e) {
            log.error(e.getMessage());
            return Optional.empty();
        }
    }


    @Override
    public List<Training> list(boolean isTrainer, TrainingFilterDTO trainingFilterDTO) {
        try (Session session = sessionFactory.openSession()) {
            var cb = session.getCriteriaBuilder();
            var cq = cb.createQuery(Training.class);
            var root = cq.from(Training.class);
            List<Predicate> predicates = new ArrayList<>();
            if (trainingFilterDTO.getDateFrom() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("date"), trainingFilterDTO.getDateFrom()));
            }
            if (trainingFilterDTO.getDateTo() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("date"), trainingFilterDTO.getDateTo()));
            }
            var join = isTrainer ? root.join("trainer").join("user") : root.join("trainee").join("user");
            predicates.add(cb.equal(join.get("username"), trainingFilterDTO.getUsername()));
            if (trainingFilterDTO.getName() != null && !trainingFilterDTO.getName().isBlank()) {
                predicates.add(cb.ilike(join.get("firstname"), trainingFilterDTO.getName()));
            }
            if (!isTrainer && trainingFilterDTO.getSpecialization() != null) {
                var trainingTypeJoin = root.join("training_type");
                predicates.add(cb.equal(trainingTypeJoin.get("training_type_name"), trainingFilterDTO.getSpecialization()));
            }
            cq.where(cb.and(predicates.toArray(new Predicate[0])));
            return session.createQuery(cq).getResultList();
        } catch (HibernateException e) {
            log.error(e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public Optional<Training> changeTrainer(Training training) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tr = session.beginTransaction();
            training = session.merge(training);
            tr.commit();
            return Optional.of(training);
        } catch (HibernateException e) {
            log.error(e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public boolean delete(Training training) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tr = session.beginTransaction();
            session.remove(training);
            tr.commit();
            return true;
        } catch (HibernateException e) {
            log.error(e.getMessage());
            return false;
        }
    }

    @Override
    public Optional<Training> getTraining(RequestTrainingDTO trainingDTO) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tr = session.beginTransaction();
            var training = session.createQuery(GET_TRAINING, Training.class)
                    .setParameter("traineeName", trainingDTO.getTraineeUsername())
                    .setParameter("trainerName", trainingDTO.getTrainerUsername())
                    .setParameter("date", trainingDTO.getDate())
                    .setParameter("duration", trainingDTO.getDuration())
                    .getResultList().stream().findFirst();
            tr.commit();
            return training;
        } catch (HibernateException e) {
            log.error(e.getMessage());
            return Optional.empty();
        }
    }


}
