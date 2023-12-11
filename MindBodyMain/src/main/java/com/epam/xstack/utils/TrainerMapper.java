package com.epam.xstack.utils;

import com.epam.xstack.model.Trainee;
import com.epam.xstack.model.Trainer;
import com.epam.xstack.model.dto.TrainerDTO;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface TrainerMapper {
    TrainerMapper INSTANCE = Mappers.getMapper(TrainerMapper.class);

    @Mapping(source = "user.username", target = "username")
    @Mapping(source = "user.firstname", target = "firstname")
    @Mapping(source = "user.lastname", target = "lastname")
    @Mapping(source = "trainingType.trainingTypeName", target = "specialization")
    @Mapping(source = "user.active", target = "isActive")
    TrainerDTO toDto(Trainer trainer);

    @Mapping(source = "user.username", target = "username")
    @Mapping(source = "user.firstname", target = "firstname")
    @Mapping(source = "user.lastname", target = "lastname")
    @Mapping(target = "isActive", ignore = true)
    TrainerDTO traineeToDto(Trainee trainee);

    @IterableMapping(qualifiedByName = "forList")
    List<TrainerDTO> toDtoForList(List<Trainer> trainers);

    @Mapping(source = "user.username", target = "username")
    @Mapping(source = "user.firstname", target = "firstname")
    @Mapping(source = "user.lastname", target = "lastname")
    @Mapping(source = "trainingType.trainingTypeName", target = "specialization")
    @Mapping(target = "isActive", ignore = true)
    @Named(value = "forList")
    TrainerDTO toDtoForList(Trainer trainer);
}


