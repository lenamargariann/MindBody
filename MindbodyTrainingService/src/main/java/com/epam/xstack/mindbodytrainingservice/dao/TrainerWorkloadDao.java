package com.epam.xstack.mindbodytrainingservice.dao;

import com.epam.xstack.mindbodytrainingservice.model.TrainerWorkloadRequest;
import com.epam.xstack.mindbodytrainingservice.model.TrainingDateProjection;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TrainerWorkloadDao extends CrudRepository<TrainerWorkloadRequest, Long> {

    Optional<TrainerWorkloadRequest> findFirstByUsername(String username);

    @Query("SELECT new com.epam.xstack.mindbodytrainingservice.model.TrainingDateProjection(" + "EXTRACT(YEAR FROM trainingDate)," + "EXTRACT(MONTH FROM trainingDate), " + "SUM(trainingDuration)) " + "FROM TrainerWorkloadRequest " + "WHERE username = :username " + "GROUP BY EXTRACT(YEAR FROM trainingDate), EXTRACT(MONTH FROM trainingDate)")
    List<TrainingDateProjection> findTrainingDatesByUserName(@Param("username") String username);

    @Modifying
    @Query("DELETE FROM TrainerWorkloadRequest t WHERE " + "t.username = :username AND " + "t.firstname = :firstname AND " + "t.lastname = :lastname AND " + "t.isActive = :isActive AND " + "t.trainingDate = :trainingDate AND " + "t.trainingDuration = :trainingDuration")
    void deleteByFields(@Param("username") String username, @Param("firstname") String firstname, @Param("lastname") String lastname, @Param("isActive") boolean isActive, @Param("trainingDate") LocalDateTime trainingDate, @Param("trainingDuration") Integer trainingDuration);
}
