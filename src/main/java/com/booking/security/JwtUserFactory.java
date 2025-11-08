package com.booking.security;


import com.booking.entities.User;
import com.booking.enums.AuthorityName;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

public final class JwtUserFactory {

    private JwtUserFactory() {
    }

    public static JwtUserDetails create(User user) {
        JwtUserDetails jwtUserDetails = new JwtUserDetails(
                user.getId(),
                user.getUsername(),
                user.getPassword() != null ? user.getPassword() : "",
                Boolean.TRUE.equals(user.getEnabled()),
                !Boolean.TRUE.equals(user.getExpired()),
                !Boolean.TRUE.equals(user.getExpired()),
                !Boolean.TRUE.equals(user.getLocked()),
                Boolean.TRUE.equals(user.getConfirmed()),
                mapToGrantedAuthorities(user.getAuthorities()));
        return jwtUserDetails.setFirstname(user.getFirstName())
                .setLastname(user.getLastName())
                .setEmail(user.getEmail());
    }

    private static Set<GrantedAuthority> mapToGrantedAuthorities(Set<AuthorityName> authorities) {
        return CollectionUtils.emptyIfNull(authorities).stream()
                .map(authority -> new SimpleGrantedAuthority(authority.name()))
                .collect(Collectors.toSet());
    }
}
