package com.epam.trainerhours.controller;

import com.epam.trainerhours.config.JwtProvider;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private static final Logger LOGGER = Logger.getLogger(AuthController.class.getName());
    private final UserDetailsService userService;
    private final JwtProvider jwtProvider;

    public AuthController(UserDetailsService userService, JwtProvider jwtProvider) {
        this.userService = userService;
        this.jwtProvider = jwtProvider;
    }

    @GetMapping("/login")
    public ResponseEntity<Map> login(@RequestParam String username,
                                     @RequestParam String password) {

        try {
            if (username == null || password == null) {
                LOGGER.info("Empty parameters: username=" + username + ", password=" + password);
                return ResponseEntity.badRequest().body(null);
            }

            UserDetails userDetails = userService.loadUserByUsername(username);

            if (userDetails == null) {
                LOGGER.info("User does not exist: username=" + username + ", password=" + password);
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok(Map.of("token", jwtProvider.generateToken(username)));
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "error on login: " + username + ", pass:" + password);
            return ResponseEntity.internalServerError().build();
        }
    }

}
