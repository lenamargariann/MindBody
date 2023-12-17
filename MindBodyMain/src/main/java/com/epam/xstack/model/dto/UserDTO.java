package com.epam.xstack.model.dto;

import com.epam.xstack.model.Role;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
public class UserDTO {
    @Pattern(regexp = "^[a-zA-Z]+\\.[a-zA-Z]+[0-9]*$", message = "Username is not valid.")
    @Size(max = 100, message = "Username must be less than 100 characters")
    @NonNull
    private String username;

    @Size(min = 4, max = 100, message = "Password should contain at least 10 characters.")
    @NonNull
    private String password;

    private Role role = Role.user;
}
