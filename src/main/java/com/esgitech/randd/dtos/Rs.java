package com.esgitech.randd.dtos;

import com.esgitech.randd.enums.Role;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Rs {
    private int status;
    private String message;

    private String token;
    private Role role;
    private String expirationTime;

    private UsDTO user;
    private List<UsDTO> users;
}