package com.epam.xstack.model.dto;

import com.epam.xstack.model.Trainer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TraineeDTO {
    @Pattern(regexp = "^[a-zA-Z]+\\.[a-zA-Z]+[0-9]*$",message = "Username is not valid.")
    private String username;

    @Pattern(regexp = "^[a-zA-Z]+$")
    @Size(max = 100, message = "First name must be less than 100 characters")
    private String firstname;

    @Pattern(regexp = "^[a-zA-Z]+$")
    @Size(max = 100, message = "Last name must be less than 100 characters")
    private String lastname;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE, pattern = "dd-MM-yyyy")
    private LocalDate dateOfBirth;

    @Size(min = 2, max = 255, message = "Address must be less than 255 characters")
    private String address;

    private List<Trainer> trainers = new ArrayList<>();

    private Boolean isActive = false;

}
