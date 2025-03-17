package com.catalin.eventmanager.service;

import com.catalin.eventmanager.documents.User;
import com.catalin.eventmanager.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("When username is null, expect empty optional")
    void test() {
        assertEquals(Optional.empty(), userService.getUserInfo(null));
    }

    @Test
    @DisplayName("When username is blank, return empty optional")
    void test2() {
        assertEquals(Optional.empty(), userService.getUserInfo(""));
    }

    @Test
    @DisplayName("When username is not found, expect empty optional")
    void test3() {
        when(userRepository.findByUsername("user")).thenReturn(Optional.empty());

        assertEquals(Optional.empty(), userService.getUserInfo("user"));
    }

    @Test
    @DisplayName("When username is valid, expect user info")
    void test4() {
        User user = new User("ID", "user", "password", "ROLE_USER");
        when(userRepository.findByUsername("user")).thenReturn(Optional.of(user));
        assertEquals(user, userService.getUserInfo("user").get());
    }

}
