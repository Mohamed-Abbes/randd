package com.esgitech.randd.dtos;

import com.esgitech.randd.enums.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ArticleDTO {

    private Long id;

    private String title;

    private String content;

    private Category category;

    private UserDTO user;

    private LocalDateTime createdAt ;

}
