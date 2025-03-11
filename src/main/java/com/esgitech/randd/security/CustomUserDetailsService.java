package com.esgitech.randd.security;

import com.esgitech.randd.entities.User;
import com.esgitech.randd.exception.NotFoundException;
import com.esgitech.randd.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByEmail(username)
                .orElseThrow(()-> new NotFoundException("User email not found"));

        return AuthUser.builder()
                .user(user)
                .build();
    }
}
