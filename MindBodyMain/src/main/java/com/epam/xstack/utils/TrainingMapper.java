package com.epam.xstack.utils;

import com.epam.xstack.model.Training;
import com.epam.xstack.model.User;
import com.epam.xstack.model.dto.TrainingDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.Objects;

@Mapper
public interface TrainingMapper {
    TrainingMapper INSTANCE = Mappers.getMapper(TrainingMapper.class);

    @Mapping(source = "name", target = "name")
    @Mapping(source = "date", target = "date")
    @Mapping(source = "type.trainingTypeName", target = "trainingType")
    @Mapping(source = "duration", target = "duration")
    @Mapping(target = "trainerName", expression = "java(mapFullName(training.getTrainer().getUser()))")
    @Mapping(target = "traineeName", expression = "java(mapFullName(training.getTrainee().getUser()))")
    TrainingDTO toDto(Training training);

    default String mapFullName(User user) {
        if (user == null) return null;
        String firstname = Objects.toString(user.getFirstname(), "");
        String lastname = Objects.toString(user.getLastname(), "");
        return String.format("%s %s", firstname, lastname).trim();
    }
}
