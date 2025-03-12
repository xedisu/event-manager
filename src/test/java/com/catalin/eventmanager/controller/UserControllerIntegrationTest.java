package com.catalin.eventmanager.controller;

import com.catalin.eventmanager.config.JwtFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(UserController.class)
public class UserControllerIntegrationTest {

    @MockitoBean
    private JwtFilter jwtFilter;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Test
    @DisplayName("When /user/info is called, expect 200 code")
    void test() throws Exception {

        MockitoAnnotations.openMocks(this);

        doNothing().when(jwtFilter).doFilterInternal(any(HttpServletRequest.class), any(HttpServletResponse.class), any(FilterChain.class));

        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .addFilters(jwtFilter)
                .build();
        HttpServletResponse httpServletResponse = mockMvc.perform(get("/user/info")).andReturn().getResponse();

        assertEquals(HttpStatus.OK.value(), httpServletResponse.getStatus());
    }
}
