package com.epam.xstack.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;


@Data
@RequiredArgsConstructor
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "user_account")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;
    @NonNull
    @Column(name = "firstname")
    private String firstname;
    @NonNull
    @Column(name = "lastname")
    private String lastname;
    @NonNull
    @Column(name = "username", unique = true)
    private String username;
    @NonNull
    @JsonIgnore
    @Column(name = "password")
    private String password;
    @Column(name = "is_active")
    private boolean isActive;
    @NonNull
    @Column(columnDefinition = "varchar(255) default 'TRAINEE'", name = "role")
    @Enumerated(value = EnumType.STRING)
    private Role role;

}
