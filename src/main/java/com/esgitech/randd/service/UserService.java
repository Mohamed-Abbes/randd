package com.esgitech.randd.service;

import com.esgitech.randd.dtos.*;
import com.esgitech.randd.entities.User;
import com.esgitech.randd.enums.Role;

import java.util.List;

public interface UserService {
    Response registerUser(RegisterRequest registerRequest);
    Response loginUser(LoginRequest loginRequest);
    Rs getAllUsers();
    Response geUserById(Long id);
    Response updateUser(Long id, UserDTO userDTO);
    Response deleteUser(Long id);
    List<User> searchUsers(String searchTerm);
    Response revokeUserRole(Long id);

    User findByEmail(String email);

    void changeUserPassword(User user, String newPassword);
}
