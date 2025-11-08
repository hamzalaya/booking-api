package com.booking.security;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class LoginAttemptService {

    @Value("${account.login.maxAttempts:3}")
    private int maxAttempts;

    private final Cache<String, Integer> attemptsCache;

    public LoginAttemptService() {
        // Initialize Caffeine cache
        this.attemptsCache = Caffeine.newBuilder()
                .expireAfterWrite(5, TimeUnit.MINUTES)
                .maximumSize(100000)
                .build();
    }

    // Called when login succeeds
    public void loginSucceeded(final String key) {
        attemptsCache.invalidate(key);
    }

    // Called when login fails
    public void loginFailed(final String key) {
        Integer attempts = attemptsCache.getIfPresent(key);
        if (attempts == null) {
            attempts = 0;
        }
        attempts++;
        attemptsCache.put(key, attempts);
    }

    // Check if user is blocked
    public boolean isBlocked(final String key) {
        Integer attempts = attemptsCache.getIfPresent(key);
        return attempts != null && attempts >= maxAttempts;
    }
}
