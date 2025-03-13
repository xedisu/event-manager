package com.catalin.eventmanager;

import com.catalin.eventmanager.utils.JwtUtils;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestConfig {

    @Bean
    public JwtUtils jwtUtils(){
        return new JwtUtils();
    }


}
