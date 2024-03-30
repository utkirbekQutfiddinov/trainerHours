package com.epam.trainerhours.controller;

import com.epam.trainerhours.config.JwtProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.HashSet;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AuthControllerTest {
    @Mock
    private UserDetailsService userService;

    @Mock
    private JwtProvider jwtProvider;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void login_Success() {
        UserDetails userDetails = new User("username", "password", new HashSet<>());
        when(userService.loadUserByUsername("username")).thenReturn(userDetails);
        when(jwtProvider.generateToken("username")).thenReturn("token");

        ResponseEntity<Map> response = authController.login("username", "password");

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().containsKey("token"));
    }

    @Test
    void login_EmptyParameters() {
        ResponseEntity<Map> response = authController.login(null, null);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void login_UserNotFound() {
        when(userService.loadUserByUsername("unknown")).thenReturn(null);

        ResponseEntity<Map> response = authController.login("unknown", "password");

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void login_InternalServerError() {
        when(userService.loadUserByUsername("username")).thenThrow(new RuntimeException());

        ResponseEntity<Map> response = authController.login("username", "password");

        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}
