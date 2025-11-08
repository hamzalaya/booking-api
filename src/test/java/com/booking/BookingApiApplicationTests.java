package com.booking;

import com.booking.security.JwtAuthenticationEntryPoint;
import com.booking.security.JwtAuthorizationTokenFilter;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
class BookingApiApplicationTests {
    @MockitoBean
    private UserDetailsService jwtUserDetailsService;

    @MockitoBean
    private JwtAuthorizationTokenFilter authenticationTokenFilter;

    @MockitoBean
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Test
    void contextLoads() {
    }

}
