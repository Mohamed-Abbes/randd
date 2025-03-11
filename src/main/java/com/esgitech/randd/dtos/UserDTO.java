package com.esgitech.randd.dtos;

import com.esgitech.randd.entities.Article;
import com.esgitech.randd.enums.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDTO {

    private Long id;

    private String fname;

    private String lname;

    private String email;

    @JsonIgnore
    private String password;

    private Role role;

    private List<Article> articles;

    private  LocalDateTime createdAt;

}
