package com.esgitech.randd.dtos;

import com.esgitech.randd.enums.Role;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response {
    private int status;
    private String message;

    private String token;
    private Role role;
    private String expirationTime;

    private UserDTO user;
    private List<UserDTO> users;

    private ArticleDTO article;
    private List<ArticleDTO> articles;

//    private final LocalDateTime timestamp = LocalDateTime.now();


}
