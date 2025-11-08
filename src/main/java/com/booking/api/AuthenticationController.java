package com.booking.api;


import com.booking.dto.JwtAccessToken;
import com.booking.dto.LoginDto;
import com.booking.holders.ApiPaths;
import com.booking.services.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.booking.holders.ApiPaths.AUTH;


@RestController
@RequestMapping(value = ApiPaths.V1)
@AllArgsConstructor
@Tag(name = "Authentication", description = "Manage authentication")
public class AuthenticationController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationController.class);
    private AuthenticationService authenticationService;


    @PostMapping(AUTH)
    @Operation(summary = "Authenticate", description = "Authenticate a user with a valid username and password")
    public JwtAccessToken authenticate(@RequestBody LoginDto loginDto) {
        LOGGER.info("Username password authentication for user {}", loginDto.getUsername());
        return authenticationService.usernamePasswordAuthentication(loginDto);
    }


}
