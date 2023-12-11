package com.epam.xstack.utils;

import com.epam.xstack.model.Trainee;
import com.epam.xstack.model.dto.TraineeDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TraineeMapper {
    TraineeMapper INSTANCE = Mappers.getMapper(TraineeMapper.class);

    @Mapping(source = "user.username", target = "username")
    @Mapping(source = "user.firstname", target = "firstname")
    @Mapping(source = "user.lastname", target = "lastname")
    @Mapping(source = "dateOfBirth", target = "dateOfBirth")
    @Mapping(source = "address", target = "address")
    @Mapping(source = "user.active", target = "isActive")
    @Mapping(target = "trainers", source = "trainers", ignore = true)
    TraineeDTO toDto(Trainee trainee);

}