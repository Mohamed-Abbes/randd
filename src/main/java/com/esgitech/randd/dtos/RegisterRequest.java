package com.esgitech.randd.dtos;

import com.esgitech.randd.enums.Role;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RegisterRequest {

    @NotBlank(message = "fname is required")
    private String fname;

    @NotBlank(message = "lname is required")
    private String lname;

    @NotBlank(message = "email is required")
    private String email;

    @NotBlank(message = "password is required")
    private String password;

    private Role role;
}
