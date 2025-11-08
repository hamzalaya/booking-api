package com.booking.entities;

import com.booking.enums.AuthorityName;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(
        name = "users",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_users_email", columnNames = "email"),
                @UniqueConstraint(name = "uk_users_username", columnNames = "username")
        }
)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User extends AbstractEntity {

    private String firstName;
    private String lastName;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private String username;
    private String phoneNumber;
    private String password;
    private Boolean locked;
    private Boolean expired;
    private Boolean confirmed;
    private Boolean enabled;
    private Boolean deleted;

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<AuthorityName> authorities = new HashSet<>();

    // Properties owned by this user
    @OneToMany(mappedBy = "owner", fetch = FetchType.LAZY)
    private Set<Property> ownedProperties;

    // Properties where this user is a co-host
    @ManyToMany(mappedBy = "coHosts", fetch = FetchType.LAZY)
    private Set<Property> coHostedProperties;
}

