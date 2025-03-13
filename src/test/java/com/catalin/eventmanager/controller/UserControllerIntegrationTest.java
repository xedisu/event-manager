package com.catalin.eventmanager.controller;

import com.catalin.eventmanager.TestConfig;
import com.catalin.eventmanager.config.JwtFilter;
import com.catalin.eventmanager.dto.request.UserInformationRequest;
import com.catalin.eventmanager.utils.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(UserController.class)
@Import(TestConfig.class)
public class UserControllerIntegrationTest {

    @Mock
    private JwtFilter jwtFilter;

    @MockitoBean
    private UserDetailsService userDetailsService;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    void setUp() throws ServletException, IOException {
        MockitoAnnotations.openMocks(this);
        doNothing().when(jwtFilter).doFilterInternal(any(HttpServletRequest.class), any(HttpServletResponse.class), any(FilterChain.class));
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .addFilters(jwtFilter)
                .build();
    }

    @Test
    @DisplayName("When /user/info is called, expect 200 code")
    void test() throws Exception {
        HttpServletResponse httpServletResponse = mockMvc.perform(get("/user/info")).andReturn().getResponse();

        assertEquals(HttpStatus.OK.value(), httpServletResponse.getStatus());
    }

    @Test
    @DisplayName("When /user/info is called with invalid JWTToken, expect 401 code")
    void testInvalidJWT() throws Exception {
        doAnswer(invocation -> {
            HttpServletResponse response = invocation.getArgument(1);
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return null;
        }).when(jwtFilter).doFilterInternal(any(HttpServletRequest.class), any(HttpServletResponse.class), any(FilterChain.class));

        HttpServletResponse httpServletResponse = mockMvc.perform(get("/user/info")).andReturn().getResponse();
        assertEquals(HttpStatus.UNAUTHORIZED.value(), httpServletResponse.getStatus());
    }


    @Test
    @DisplayName("When /user/info is called with an invalid body, expect 400 code")
    void test3() throws Exception {
        String token = jwtUtils.generateToken("testUsername", "testRole");

        RequestBuilder request = get("/user/info").header("Authorization", "Bearer " + token)
                .param("username", "");

        HttpServletResponse httpServletResponse = mockMvc.perform(request).andReturn().getResponse();
        assertEquals(HttpStatus.BAD_REQUEST.value(), httpServletResponse.getStatus());
    }
}
