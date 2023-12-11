package com.epam.xstack.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@RequiredArgsConstructor
@NoArgsConstructor
@Entity(name = "training")
@AllArgsConstructor
public class Training {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NonNull
    @ManyToOne
    @JoinColumn(name = "trainee_id")
    private Trainee trainee;
    @NonNull
    @ManyToOne
    @JoinColumn(name = "trainer_id")
    private Trainer trainer;
    @NonNull
    @Column(name = "name")
    private String name;
    @NonNull
    @ManyToOne
    @JoinColumn(name = "type_id")
    private TrainingType type;
    @NonNull
//    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date")
    private LocalDateTime date;
    @NonNull
    @Column(name = "duration")
    private Integer duration;
    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        StringBuilder sb = new StringBuilder("Training{");

        if (id != null) sb.append("id=").append(id);
        if (trainee != null) sb.append(", trainee=").append(trainee);
        if (trainer != null) sb.append(", trainer=").append(trainer);
        if (name != null) sb.append(", name='").append(name).append('\'');
        if (type != null) sb.append(", type=").append(type);
        if (date != null) sb.append(", date='").append(formatter.format(date)).append('\'');
        if (duration != null) sb.append(", duration=").append(duration);

        sb.append('}');
        return sb.toString();
    }
}

