package com.esgitech.randd.service.impl;

import com.esgitech.randd.entities.PasswordResetToken;
import com.esgitech.randd.entities.User;
import com.esgitech.randd.repository.PasswordResetTokenRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
public class PasswordResetTokenService {

    private final PasswordResetTokenRepository tokenRepository;

    public PasswordResetTokenService(PasswordResetTokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    public String createPasswordResetToken(User user) {
        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = new PasswordResetToken(token, user);
        tokenRepository.save(resetToken);
        return token;
    }

    public boolean validatePasswordResetToken(String token) {
        PasswordResetToken resetToken = tokenRepository.findByToken(token);
        return resetToken != null && !isTokenExpired(resetToken);
    }

    private boolean isTokenExpired(PasswordResetToken token) {
        return token.getExpiryDate().before(new Date());
    }

    public User getUserByPasswordResetToken(String token) {
        PasswordResetToken resetToken = tokenRepository.findByToken(token);
        return resetToken != null ? resetToken.getUser() : null;
    }

    public void deleteToken(String token) {
        PasswordResetToken resetToken = tokenRepository.findByToken(token);
        if (resetToken != null) {
            tokenRepository.delete(resetToken);
        }
    }
}