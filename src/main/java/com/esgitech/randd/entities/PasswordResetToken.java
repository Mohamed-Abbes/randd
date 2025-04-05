package com.esgitech.randd.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Entity
@AllArgsConstructor
@Data
@Builder
public class PasswordResetToken {
    private static final int EXPIRATION = 24 * 60 * 60 * 1000; // 24 hours

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String token;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    private Date expiryDate;

    public PasswordResetToken() {
        this.expiryDate = new Date(System.currentTimeMillis() + EXPIRATION);
    }

    public PasswordResetToken(String token, User user) {
        this.token = token;
        this.user = user;
        this.expiryDate = new Date(System.currentTimeMillis() + EXPIRATION);
    }

    // Getters and setters
}