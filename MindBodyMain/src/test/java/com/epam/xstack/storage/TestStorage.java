package com.epam.xstack.storage;

import com.epam.xstack.model.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class TestStorage {
    static public List<User> users = List.of(
            new User(1L, "Alice", "Smith", "alice.smith", "password", true, Role.TRAINEE),
            new User(2L, "Bob", "Johnson", "bob.johnson", "password", true,Role.TRAINEE),
            new User(3L, "Charlie", "Brown", "charlie.brown", "password", true,Role.TRAINEE),

            new User(4L, "Dan", "Taylor", "dan.taylor", "password", true,Role.TRAINER),
            new User(5L, "Eva", "White", "eva.white", "password", true,Role.TRAINER),
            new User(6L, "Frank", "Miller", "frank.miller", "password", true,Role.TRAINER)
    );
    public static List<TrainingType> trainingTypes = List.of(
            new TrainingType(1L, "Aerobics"),
            new TrainingType(2L, "Yoga")
    );
    public static List<Trainer> trainers = List.of(
            new Trainer(1L, trainingTypes.get(0), users.get(3), List.of()),
            new Trainer(2L, trainingTypes.get(1), users.get(4), List.of()),
            new Trainer(3L, trainingTypes.get(0), users.get(5), List.of())
    );
    static LocalDate sampleDate = LocalDate.now();
    public static List<Trainee> trainees = List.of(
            new Trainee(1L, sampleDate, "Address 1", users.get(0), List.of(), List.of()),
            new Trainee(2L, sampleDate, "Address 2", users.get(1), List.of(), List.of()),
            new Trainee(3L, sampleDate, "Address 3", users.get(2), List.of(), List.of())
    );

    static LocalDateTime sampleDateTime = LocalDateTime.now();
    public static List<Training> trainings = List.of(
            new Training(1L, trainees.get(0), trainers.get(0), "Training 1", trainers.get(0).getTrainingType(), sampleDateTime, 60),
            new Training(2L, trainees.get(1), trainers.get(1), "Training 2", trainers.get(1).getTrainingType(), sampleDateTime, 65),
            new Training(3L, trainees.get(2), trainers.get(2), "Training 3", trainers.get(2).getTrainingType(), sampleDateTime, 60)

    );
}
