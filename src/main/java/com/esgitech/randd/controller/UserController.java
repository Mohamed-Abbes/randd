package com.esgitech.randd.controller;


import com.esgitech.randd.dtos.Response;
import com.esgitech.randd.dtos.Rs;
import com.esgitech.randd.dtos.UserDTO;
import com.esgitech.randd.entities.User;
import com.esgitech.randd.enums.Role;
import com.esgitech.randd.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @GetMapping("/all")
    public ResponseEntity<Rs> getAllUsers() {
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

    @DeleteMapping ("/delete/{id}")     //Role based access control @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<Response> deleteUser(@PathVariable  Long id) {
        return ResponseEntity.ok(userService.deleteUser(id));
    }

    @GetMapping("/search")
    public List<User> searchUsers(@RequestParam String term) {
        return userService.searchUsers(term);
    }

    @GetMapping("/role/{id}")
    public ResponseEntity<Response> revokeUserRole(@PathVariable Long id) {
        System.out.println(id);
        return ResponseEntity.ok(userService.revokeUserRole(id));
    }

}

