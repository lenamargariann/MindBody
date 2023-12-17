package com.epam.xstack.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import lombok.*;


@ToString
@Data
@Entity(name = "training_type")
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
public class TrainingType {
    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Nonnull
    @Column(name = "training_type_name", unique = true)
    private String trainingTypeName;


}

