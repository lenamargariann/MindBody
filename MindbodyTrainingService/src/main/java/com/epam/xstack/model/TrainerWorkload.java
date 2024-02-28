package com.epam.xstack.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.Map;

@Data
@Builder
@Document(collection = "trainerWorkloads")
public class TrainerWorkload {
    @MongoId
    private String id;
    @Pattern(regexp = "^[a-zA-Z]+\\.[a-zA-Z]+[0-9]*$", message = "Username is not valid.")
    @Field(name = "trainerUsername")
    private String trainerUsername;
    @Pattern(regexp = "^[a-zA-Z]+$")
    @Field(name = "trainerFirstname")
    private String trainerFirstname;
    @Pattern(regexp = "^[a-zA-Z]+$")
    @Field(name = "trainerLastname")
    private String trainerLastname;
    @JsonProperty(value = "isActive")
    @Field(name = "isActive")
    private boolean isActive;
    private Map<String, Map<String, Integer>> years;

}
