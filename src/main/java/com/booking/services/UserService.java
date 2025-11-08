package com.booking.services;

import com.booking.dto.RegistrationDto;
import com.booking.dto.UserDto;
import com.booking.entities.User;
import com.booking.exceptions.BookingException;
import com.booking.exceptions.error.ExceptionCode;
import com.booking.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    public UserDto register(RegistrationDto registrationDto) {
        LOGGER.info("Register new account for user with email {}", registrationDto.getEmail());
        // Check if user already exists
        if (userRepository.findByUsername(registrationDto.getEmail()).isPresent()) {
            throw new BookingException(ExceptionCode.API_BAD_REQUEST, "account.email.used");
        }

        // Create and save new user
        User user = createUser(registrationDto);
        return UserDto.builder()
                .firstname(user.getFirstName())
                .lastname(user.getLastName())
                .email(user.getEmail())
                .username(user.getUsername()).build();


    }

    private User createUser(RegistrationDto registrationDto) {
        User user = User.builder()
                .email(registrationDto.getEmail())
                .username(registrationDto.getEmail())
                .firstName(registrationDto.getFirstname())
                .lastName(registrationDto.getLastname())
                .password(passwordEncoder.encode(registrationDto.getPassword()))
                .confirmed(true) // we can add later a confirmation step
                .enabled(true)
                .locked(false)
                .expired(false)
                .deleted(false).build();
        return userRepository.save(user);
    }
}
