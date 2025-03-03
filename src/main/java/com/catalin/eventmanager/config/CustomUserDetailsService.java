package com.catalin.eventmanager.config;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // TODO: Fetch user from MongoDB instead of hardcoding
        if ("testuser".equals(username)) {
            return new User("testuser", "{noop}password", Collections.emptyList());
        }
        throw new UsernameNotFoundException("User not found: " + username);
    }
}