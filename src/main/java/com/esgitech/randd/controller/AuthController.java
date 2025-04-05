package com.esgitech.randd.controller;

import com.esgitech.randd.dtos.LoginRequest;
import com.esgitech.randd.dtos.RegisterRequest;
import com.esgitech.randd.dtos.Response;
import com.esgitech.randd.service.UserService;
import com.esgitech.randd.service.impl.UserServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @PostMapping("/register")
    public ResponseEntity<Response> registerUser(@RequestBody @Valid RegisterRequest  registerRequest) {
        logger.debug("User to be saved: {}", registerRequest.toString());
        return ResponseEntity.ok(userService.registerUser(registerRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<Response> loginUser(@RequestBody @Valid LoginRequest loginRequest) {
        log.info("Controller login request: {}", loginRequest);
        return ResponseEntity.ok(userService.loginUser(loginRequest));
    }
}
