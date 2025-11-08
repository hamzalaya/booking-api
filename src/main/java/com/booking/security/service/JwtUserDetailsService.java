package com.booking.security.service;


import com.booking.entities.User;
import com.booking.repositories.UserRepository;
import com.booking.security.JwtUserFactory;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class JwtUserDetailsService implements UserDetailsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtUserDetailsService.class);
    private UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LOGGER.debug("Load user by username {}", username);
        Optional<User> user = userRepository.findByUsername(username);
        return user.map(JwtUserFactory::create).orElseThrow(() -> new UsernameNotFoundException(String.format("No user found with username '%s'.", username)));
    }
}
