package com.epam.xstack.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.relational.core.mapping.Table;

@Data
@RequiredArgsConstructor
@NoArgsConstructor
@AllArgsConstructor
//@Entity
@Table("user_account")
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

    @Column(columnDefinition = "varchar(255) default 'user'")
    @Enumerated(value = EnumType.STRING)
    private Role role;

}
