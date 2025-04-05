package com.esgitech.randd.controller;

import com.esgitech.randd.dtos.PasswordResetRequest;
import com.esgitech.randd.entities.User;
import com.esgitech.randd.service.UserService;
import com.esgitech.randd.service.impl.EmailService;
import com.esgitech.randd.service.impl.PasswordResetTokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class PasswordResetController {

    private final UserService userService;
    private final EmailService emailService;
    private final PasswordResetTokenService tokenService;

    public PasswordResetController(UserService userService,
                                   EmailService emailService,
                                   PasswordResetTokenService tokenService) {
        this.userService = userService;
        this.emailService = emailService;
        this.tokenService = tokenService;
    }

    @PostMapping("/request-password-reset")
    public ResponseEntity<?> requestPasswordReset(@RequestBody PasswordResetRequest request) {
        User user = userService.findByEmail(request.getEmail());

        if (user == null) {
            // For security reasons, don't reveal if the email exists or not
            return ResponseEntity.ok().body(Map.of("message", "If this email exists, a password reset link has been sent"));
        }

        String token = tokenService.createPasswordResetToken(user);
        emailService.sendPasswordResetEmail(user.getEmail(), token);

        return ResponseEntity.ok().body(Map.of("message", "If this email exists, a password reset link has been sent"));
    }


    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody PasswordResetRequest request) {
        String token = request.getToken();
        String newPassword = request.getNewPassword();

        if (!tokenService.validatePasswordResetToken(token)) {
            return ResponseEntity.badRequest().body(Map.of("message", "Invalid or expired token"));
        }

        User user = tokenService.getUserByPasswordResetToken(token);
        if (user == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "User not found"));
        }

        userService.changeUserPassword(user, newPassword);
        tokenService.deleteToken(token);

        return ResponseEntity.ok().body(Map.of("message", "Password reset successfully"));
    }
}