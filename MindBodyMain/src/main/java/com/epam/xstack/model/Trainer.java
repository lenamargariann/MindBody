package com.epam.xstack.model;

import com.epam.xstack.model.dto.TrainerDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import java.util.List;


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

    public Trainer updateFromDTO(TrainerDTO trainerDTO, TrainingType trainingType) {
        if (trainerDTO.getFirstname() != null) user.setFirstname(trainerDTO.getFirstname());
        if (trainerDTO.getLastname() != null) user.setLastname(trainerDTO.getLastname());
        if (trainingType != null) setTrainingType(trainingType);
        return this;
    }
}

