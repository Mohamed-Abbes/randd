package com.esgitech.randd.service.impl;

import com.esgitech.randd.dtos.LoginRequest;
import com.esgitech.randd.dtos.RegisterRequest;
import com.esgitech.randd.dtos.Response;
import com.esgitech.randd.dtos.UserDTO;
import com.esgitech.randd.entities.User;
import com.esgitech.randd.enums.Role;
import com.esgitech.randd.exception.InvalidCredentialsException;
import com.esgitech.randd.exception.NotFoundException;
import com.esgitech.randd.repository.UserRepository;
import com.esgitech.randd.security.JwtUtils;
import com.esgitech.randd.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Response registerUser(RegisterRequest registerRequest) {
        User userTosave = User.builder()
                .fname(registerRequest.getFname())
                .lname(registerRequest.getLname())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(registerRequest.getRole() != null ? registerRequest.getRole() : Role.USER)
                .build();
        userRepository.save(userTosave);
        return Response.builder()
                .status(200)
                .message("User was sucessfully registred")
                .build();
    }

    @Override
    public Response loginUser(LoginRequest loginRequest) {
        log.info("Login request received");
        User user = userRepository.findUserByEmail(loginRequest.getEmail())
                .orElseThrow(()-> new NotFoundException("Email not found"));

        if(!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())){
            throw new InvalidCredentialsException("Invalid password");
        }
        String token = jwtUtils.generateToken(user.getEmail());
        return Response.builder()
                .status(200)
                .message("User Logged in successfully")
                .role(user.getRole())
                .token(token)
                .expirationTime("6 months")
                .build();
    }

    @Override
    public Response getAllUsers() {
        log.info("Get all users");
        List<User> users = userRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
        List<UserDTO> usersDTOS = modelMapper.map(users , new TypeToken<List<UserDTO>>(){}.getType());
        return Response.builder()
                .status(200)
                .message("success")
                .users(usersDTOS)
                .build();
    }

    @Override
    public Response geUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(()->new NotFoundException("User not found"));

        UserDTO userDTO = modelMapper.map(user, UserDTO.class);

        return Response.builder()
                .status(200)
                .message("success")
                .user(userDTO)
                .build();
    }


    @Override
    public Response updateUser(Long id, UserDTO userDTO) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(()->new NotFoundException("User not found"));

        if(userDTO.getFname() != null) existingUser.setFname(userDTO.getFname());
        if(userDTO.getLname() != null) existingUser.setLname(userDTO.getLname());
        if(userDTO.getEmail() != null) existingUser.setEmail(userDTO.getEmail());
        if(userDTO.getRole() != null) existingUser.setRole(userDTO.getRole());

        if(userDTO.getPassword() != null && !userDTO.getPassword().isEmpty())
            existingUser.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        userRepository.save(existingUser);
        return Response.builder()
                .status(200)
                .message("User successfully updated")
                .user(userDTO)
                .build();
    }

    @Override
    public Response deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(()->new NotFoundException("User not found"));

        userRepository.delete(user);
        return Response.builder()
                .status(200)
                .message("User successfully delted")
                .build();
    }
}
