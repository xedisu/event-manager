package com.catalin.eventmanager.controller;

import com.catalin.eventmanager.config.JwtFilter;
import com.catalin.eventmanager.config.SecurityConfig;
import com.catalin.eventmanager.documents.User;
import com.catalin.eventmanager.service.UserService;
import com.catalin.eventmanager.utils.JwtUtils;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(UserController.class)
@Import(SecurityConfig.class)
public class UserControllerIntegrationTest {
    @MockitoBean
    JwtUtils jwtUtils;
    @InjectMocks
    private JwtFilter jwtFilter;
    @MockitoBean
    private UserDetailsService userDetailsService;
    @MockitoBean
    private UserService userService;
    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("When /user/info is called, expect 200 code")
    void test() throws Exception {
        UserDetails userDetails =
                new org.springframework.security.core.userdetails.User(
                        "user", "password", List.of(new SimpleGrantedAuthority("ROLE_USER")));

        User user = new User("ID",
                "user", "password", "ROLE_USER");
        when(userService.getUserInfo("user")).thenReturn(java.util.Optional.of(user));
        
        String token = "test-token";
        when(userDetailsService.loadUserByUsername("user")).thenReturn(userDetails);

        when(jwtUtils.validateToken(token)).thenReturn(true);
        when(jwtUtils.extractUsername(token)).thenReturn("user");
        when(jwtUtils.extractAuthorities(token))
                .thenReturn(List.of(new SimpleGrantedAuthority("ROLE_USER")));

        HttpServletResponse httpServletResponse =
                mockMvc.perform(
                                get("/user/info")
                                        .param("username", "user")
                                        .header("Authorization", "Bearer " + token))
                        .andReturn()
                        .getResponse();

        assertEquals(HttpStatus.OK.value(), httpServletResponse.getStatus());
    }

    @Test
    @DisplayName("When /user/info is called with no JWTToken, expect 403 code")
    void test2() throws Exception {
        HttpServletResponse httpServletResponse =
                mockMvc.perform(get("/user/info").param("username", "user2"))
                        .andReturn()
                        .getResponse();

        assertEquals(HttpStatus.FORBIDDEN.value(), httpServletResponse.getStatus());
    }

    @Test
    @DisplayName("When /user/info is called with expired JWTToken, expect 403 code")
    void test3() throws Exception {
        String token = jwtUtils.generateToken("user", "ROLE_USER");
        when(jwtUtils.validateToken(token)).thenReturn(false);

        HttpServletResponse httpServletResponse =
                mockMvc.perform(
                                get("/user/info")
                                        .param("username", "user3")
                                        .header("Authorization", "Bearer " + token))
                        .andReturn()
                        .getResponse();

        assertEquals(HttpStatus.FORBIDDEN.value(), httpServletResponse.getStatus());
    }

    @Test
    @DisplayName("When /user/info is called with an invalid body, expect 400 code")
    void test4() throws Exception {
        UserDetails userDetails =
                new org.springframework.security.core.userdetails.User(
                        "user", "password", List.of(new SimpleGrantedAuthority("ROLE_USER")));

        String token = "test-token";
        when(userDetailsService.loadUserByUsername("user")).thenReturn(userDetails);

        when(jwtUtils.validateToken(token)).thenReturn(true);
        when(jwtUtils.extractUsername(token)).thenReturn("user");
        when(jwtUtils.extractAuthorities(token))
                .thenReturn(List.of(new SimpleGrantedAuthority("ROLE_USER")));

        HttpServletResponse httpServletResponse =
                mockMvc.perform(get("/user/info").header("Authorization", "Bearer " + token))
                        .andReturn()
                        .getResponse();

        assertEquals(HttpStatus.BAD_REQUEST.value(), httpServletResponse.getStatus());
    }

    @Test
    @DisplayName("When /user/info is called with an invalid role, expect 403 code")
    void test5() throws Exception {
        UserDetails userDetails =
                new org.springframework.security.core.userdetails.User(
                        "user", "password", List.of(new SimpleGrantedAuthority("ROLE_USER")));

        String token = "test-token";
        when(userDetailsService.loadUserByUsername("user")).thenReturn(userDetails);

        when(jwtUtils.validateToken(token)).thenReturn(true);
        when(jwtUtils.extractUsername(token)).thenReturn("user");
        when(jwtUtils.extractAuthorities(token))
                .thenReturn(List.of(new SimpleGrantedAuthority("invalid_role")));

        HttpServletResponse httpServletResponse =
                mockMvc.perform(
                                get("/user/info")
                                        .param("username", "user")
                                        .header("Authorization", "Bearer " + token))
                        .andReturn()
                        .getResponse();

        assertEquals(HttpStatus.FORBIDDEN.value(), httpServletResponse.getStatus());
    }


}
