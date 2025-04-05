package com.esgitech.randd.entities;

import com.esgitech.randd.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
@Data
@Builder
public class User{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Long id;

    @NotBlank(message="first name is required")
    @NotNull
    private String firstName;

    @NotBlank(message="last name is required")
    @NotNull
    private String lastName;

    private String userName;

    private String email;

    @NotBlank(message="last name is required")
    @NotNull
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "user")
    private List<Article> articlesSet = new ArrayList<>();

    @Column(name = "created_at")
    @CreatedDate
    private final LocalDateTime createdAt = LocalDateTime.now();

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", username='" + userName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", role=" + role +
                '}';
    }
}
