package com.epam.xstack.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestTraineeDTO {
    @Pattern(regexp = "^[a-zA-Z]+$")
    @Size(max = 100, message = "Username must be less than 100 characters")
    private String firstname;
    @Pattern(regexp = "^[a-zA-Z]+$")
    @Size(max = 100, message = "Last name must be less than 100 characters")
    private String lastname;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE, pattern = "dd-MM-yyyy")
    private LocalDate dateOfBirth;

    @Size(min = 2, max = 255, message = "Address must be less than 255 characters")
    private String address;

    @Pattern(regexp = "^(?=.*[A-Z]).{8,}$", message = "Password must be at least 8 characters long and include at least one uppercase letter.")
    private String password;

}
