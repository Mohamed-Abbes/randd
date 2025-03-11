package com.esgitech.randd.controller;


import com.esgitech.randd.dtos.Response;
import com.esgitech.randd.dtos.UserDTO;
import com.esgitech.randd.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @GetMapping("/all")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<Response> getAllUsers() {
        log.info("Get all users");
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response> getUserById(@PathVariable Long id) {
        log.info("Get user by id: {}", id);
        return ResponseEntity.ok(userService.geUserById(id));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Response> updateUser(@PathVariable  Long id, @RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(userService.updateUser(id, userDTO));
    }

    @DeleteMapping ("/delete/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<Response> deleteUser(@PathVariable  Long id) {
        return ResponseEntity.ok(userService.deleteUser(id));
    }

}

