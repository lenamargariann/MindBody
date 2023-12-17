package com.epam.xstack.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrainerDTO {

    @Pattern(regexp = "^[a-zA-Z]+\\.[a-zA-Z]+[0-9]*$",message = "Username is not valid.")
    @JsonProperty("username")
    private String username;

    @Pattern(regexp = "^[a-zA-Z]+$")
    private String firstname;

    @Pattern(regexp = "^[a-zA-Z]+$")
    private String lastname;

    @NotBlank(message = "Specialization is required")
    private String specialization;

    private Boolean isActive = false;

}
