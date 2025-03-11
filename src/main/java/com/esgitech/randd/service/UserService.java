package com.esgitech.randd.service;

import com.esgitech.randd.dtos.LoginRequest;
import com.esgitech.randd.dtos.RegisterRequest;
import com.esgitech.randd.dtos.Response;
import com.esgitech.randd.dtos.UserDTO;

public interface UserService {
    Response registerUser(RegisterRequest registerRequest);
    Response loginUser(LoginRequest loginRequest);
    Response getAllUsers();
    Response geUserById(Long id);
    Response updateUser(Long id, UserDTO userDTO);
    Response deleteUser(Long id);
}
