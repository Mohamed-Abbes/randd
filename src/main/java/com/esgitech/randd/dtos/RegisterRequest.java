package com.esgitech.randd.dtos;

import com.esgitech.randd.enums.Role;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RegisterRequest {

    @NotBlank(message = "fname is required")
    private String firstName;

    @NotBlank(message = "lname is required")
    private String lastName;

    @NotBlank(message = "email is required")
    private String email;

    @NotBlank(message = "username is required")
    private String userName;

    @NotBlank(message = "password is required")
    private String password;

    private Role role;
}
