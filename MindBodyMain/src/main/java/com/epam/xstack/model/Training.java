package com.epam.xstack.model;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

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
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm")
     @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm")
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

