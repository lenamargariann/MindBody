package com.epam.xstack.model.dto;


import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class PasswordChangeDTO {
    @Pattern(regexp = "^[a-zA-Z]+\\.[a-zA-Z]+[0-9]*$", message = "Username is not valid.")
    private String username;

    @Size(min = 4, message = "Password should contain at least 10 characters.")
    private String oldPassword;

    @Size(min = 10, max = 100, message = "Password should contain at least 10 characters.")
    private String newPassword;

}
