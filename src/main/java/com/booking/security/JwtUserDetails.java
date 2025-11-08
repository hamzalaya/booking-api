package com.booking.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.Set;


public class JwtUserDetails extends User {

    private final Long id;
    private final String username;
    private String displayName;
    private String firstname;
    private String lastname;
    private String email;
    private final Set<GrantedAuthority> authorities;
    private final boolean enabled;
    private boolean locked;
    private boolean expired;
    private final boolean accountNonLocked;
    private final boolean credentialsNonExpired;
    private final boolean accountNonExpired;
    private boolean confirmed;


    public JwtUserDetails(Long id, String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Set<GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.username = username;
        this.enabled = enabled;
        this.accountNonExpired = accountNonExpired;
        this.credentialsNonExpired = credentialsNonExpired;
        this.accountNonLocked = accountNonLocked;
        this.authorities = authorities;
        this.expired = !accountNonExpired;
        this.locked = !accountNonLocked;
        this.id = id;
    }

    public JwtUserDetails(Long id, String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, boolean confirmed, Set<GrantedAuthority> authorities) {
        this(id, username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.expired = !accountNonExpired;
        this.confirmed = confirmed;
        this.locked = !accountNonLocked;
    }


    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getEmail() {
        return email;
    }

    public boolean isLocked() {
        return locked;
    }

    public boolean isExpired() {
        return expired;
    }

    public JwtUserDetails setDisplayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

    public JwtUserDetails setFirstname(String firstname) {
        this.firstname = firstname;
        return this;
    }

    public JwtUserDetails setLastname(String lastname) {
        this.lastname = lastname;
        return this;
    }

    public JwtUserDetails setEmail(String email) {
        this.email = email;
        return this;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public Long getId() {
        return id;
    }
}
