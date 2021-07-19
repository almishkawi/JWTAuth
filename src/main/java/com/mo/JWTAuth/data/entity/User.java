package com.mo.JWTAuth.data.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Entity
@Data
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "First name must be at least 2 characters")
    @Length.List({
            @Length(min = 2, message = "First name must be at least 2 characters"),
            @Length(max = 100, message = "First name can't be longer than 100 characters")
    })
    private String firstName;

    @NotBlank(message = "Last name must be at least 2 characters")
    @Length.List({
            @Length(min = 2, message = "Last name must be at least 2 characters"),
            @Length(max = 100, message = "Last name can't be longer than 100 characters")
    })
    private String lastName;

    @NotBlank(message = "Email is invalid")
    @Email(message = "Email is invalid")
    @Length.List({
            @Length(min = 5, message = "Email is invalid"),
            @Length(max = 150, message = "Email is too long")
    })
    private String email;

    @NotBlank(message = "Password can't be blank")
    private String password;

    @Column(nullable = false)
    private Boolean emailVerified = false;
}
