package com.booking.utils;


import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import java.util.Locale;

public interface UserContext {


    static String getLanguage() {
        return StringUtils.defaultIfBlank(RequestHeaders.get("Accept-Language"), Locale.ENGLISH.getLanguage());
    }


    static String userName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof UsernamePasswordAuthenticationToken) {
            if (authentication.getPrincipal() instanceof User) {
                return ((User) authentication.getPrincipal()).getUsername();
            } else {
                return (String) authentication.getPrincipal();
            }
        }
        return null;
    }
}
