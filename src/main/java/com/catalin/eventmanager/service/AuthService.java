package com.catalin.eventmanager.service;

import com.catalin.eventmanager.documents.User;
import com.catalin.eventmanager.repository.UserRepository;
import com.catalin.eventmanager.utils.JwtUtils;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final JwtUtils jwtUtil;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final AuthenticationManager authenticationManager;

    public AuthService(
            UserRepository userRepository,
            JwtUtils jwtUtil,
            AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
    }

    public String register(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (user.getRole() == null || user.getRole().isEmpty()) {
            user.setRole("ROLE_USER");
        } else if (!user.getRole().startsWith("ROLE_")) {
            user.setRole("ROLE_" + user.getRole());
        }
        userRepository.save(user);
        return "User registered successfully!";
    }

    public String login(String username, String password) {
        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(username, password));
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user =
                userRepository
                        .findByUsername(userDetails.getUsername())
                        .orElseThrow(() -> new RuntimeException("User not found"));
        return jwtUtil.generateToken(user.getUsername(), user.getRole());
    }
}
