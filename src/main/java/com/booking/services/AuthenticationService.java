package com.booking.services;

import com.booking.dto.JwtAccessToken;
import com.booking.dto.LoginDto;
import com.booking.exceptions.BookingException;
import com.booking.exceptions.error.ExceptionCode;
import com.booking.security.JwtTokenUtil;
import com.booking.security.JwtUserDetails;
import com.booking.security.LoginAttemptService;
import com.booking.utils.Assert;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthenticationService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationService.class);
    private AuthenticationManager authenticationManager;
    private LoginAttemptService loginAttemptService;

    public JwtAccessToken usernamePasswordAuthentication(LoginDto loginDto) {
        LOGGER.info("Username password authentication for user {}", loginDto.getUsername());
        Assert.assertNotNull(loginDto.getUsername(), ExceptionCode.API_UNAUTHORIZED, "authentication.bad.credentials");
        Assert.assertNotNull(loginDto.getPassword(), ExceptionCode.API_UNAUTHORIZED, "authentication.bad.credentials");

        JwtUserDetails authenticated = authenticate(loginDto.getUsername(), loginDto.getPassword());
        final String token = JwtTokenUtil.generateToken(authenticated);
        return new JwtAccessToken(token);
    }

    private JwtUserDetails authenticate(String username, String password) {
        try {
            if (loginAttemptService.isBlocked(username)) {
                throw new BookingException(ExceptionCode.API_UNAUTHORIZED, "authentication.account.blocked");
            }
            JwtUserDetails jwtUserDetails = (JwtUserDetails) authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password)).getPrincipal();
            if (!jwtUserDetails.isConfirmed()) {
                throw new BookingException(ExceptionCode.API_UNAUTHORIZED, "authentication.account.not.confirmed");
            }
            loginAttemptService.loginSucceeded(username);
            return jwtUserDetails;
        } catch (BadCredentialsException | UsernameNotFoundException ex) {
            loginAttemptService.loginFailed(username);
            throw new BookingException(ExceptionCode.API_UNAUTHORIZED, "authentication.bad.credentials", ex);
        } catch (DisabledException ex) {
            throw new BookingException(ExceptionCode.API_UNAUTHORIZED, "authentication.account.disabled", ex);
        } catch (LockedException ex) {
            throw new BookingException(ExceptionCode.API_UNAUTHORIZED, "authentication.account.locked", ex);
        } catch (AccountExpiredException ex) {
            throw new BookingException(ExceptionCode.API_UNAUTHORIZED, "authentication.account.expired", ex);
        } catch (AuthenticationException ex) {
            throw new BookingException(ExceptionCode.API_UNAUTHORIZED, "authentication.error", ex);
        }
    }

}
