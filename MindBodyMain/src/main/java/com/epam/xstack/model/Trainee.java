package com.epam.xstack.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity(name = "trainee")
@RequiredArgsConstructor
@NoArgsConstructor
@AllArgsConstructor
public class Trainee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;
    @NonNull
    @Column(name = "date_of_birth")
    @Temporal(TemporalType.DATE)
    private LocalDate dateOfBirth;
    @NonNull
    @Column(name = "address")
    private String address;
    @OneToOne(fetch = FetchType.EAGER, orphanRemoval = true)
    @NonNull
    @JoinColumn(name = "user_id")
    @Cascade(CascadeType.ALL)
    private User user;
    @JsonIgnore
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "trainee", orphanRemoval = true)
    private List<Training> trainings = new ArrayList<>();

    @Cascade({CascadeType.MERGE, CascadeType.PERSIST})
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "trainee_trainer",
            joinColumns = @JoinColumn(name = "trainee_id"),
            inverseJoinColumns = @JoinColumn(name = "trainer_id")
    )
    private List<Trainer> trainers = new ArrayList<>();
}
