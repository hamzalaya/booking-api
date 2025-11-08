package com.booking.api;

import com.booking.dto.RegistrationDto;
import com.booking.dto.UserDto;
import com.booking.holders.ApiPaths;
import com.booking.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.booking.holders.ApiPaths.REGISTER;

@RestController
@RequestMapping(value = ApiPaths.V1)
@Tag(name = "Accounts", description = "Operations related to user accounts")
@AllArgsConstructor
public class AccountController {
    private UserService userService;

    @PostMapping(REGISTER)
    @Operation(summary = "Signing up", description = "Registers a new user account")
    public ResponseEntity<UserDto> register(@RequestBody RegistrationDto registrationDto) {
        UserDto user = userService.register(registrationDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }
}
