package com.epam.xstack.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestTrainerDTO {

    @Pattern(regexp = "^[a-zA-Z]+$")
    private String firstname;

    @Pattern(regexp = "^[a-zA-Z]+$")
    private String lastname;

    @NotBlank(message = "Specialization is required")
    private String specialization;

    @Pattern(regexp = "^(?=.*[A-Z]).{8,}$", message = "Password must be at least 8 characters long and include at least one uppercase letter.")
    private String password;

}