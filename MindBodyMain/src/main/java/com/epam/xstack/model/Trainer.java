package com.epam.xstack.model;

import com.epam.xstack.model.dto.TrainerDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import java.util.List;
import java.util.Optional;


@Data
@Entity(name = "trainer")
@RequiredArgsConstructor
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Trainer {
    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @NonNull
    @JoinColumn(name = "training_type_id")
    private TrainingType trainingType;

    @NonNull
    @Cascade(CascadeType.ALL)
    @OneToOne(fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "trainer", orphanRemoval = true)
    private List<Training> trainings;

    public Trainer updateFromDTO(TrainerDTO trainerDTO, Optional<TrainingType> trainingType) {
        if (trainerDTO.getFirstname() != null) user.setFirstname(trainerDTO.getFirstname());
        if (trainerDTO.getLastname() != null) user.setLastname(trainerDTO.getLastname());
        trainingType.ifPresent(this::setTrainingType);
        return this;
    }
}

