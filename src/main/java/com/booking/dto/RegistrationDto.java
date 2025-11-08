package com.booking.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RegistrationDto {
    @NotNull
    private String firstname;
    @NotNull
    private String lastname;
    @NotNull
    private String email;
    @NotNull
    private String password;
}
