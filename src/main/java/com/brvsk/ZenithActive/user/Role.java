package com.brvsk.ZenithActive.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.brvsk.ZenithActive.user.Permission.*;

@RequiredArgsConstructor
public enum Role {
    MEMBER(
            Set.of(
                    MEMBER_READ,
                    MEMBER_UPDATE,
                    MEMBER_DELETE,
                    MEMBER_CREATE
            )
    ),
    EMPLOYEE(
            Set.of(
                    EMPLOYEE_READ,
                    EMPLOYEE_UPDATE,
                    EMPLOYEE_DELETE,
                    EMPLOYEE_CREATE
            )
    ),
    INSTRUCTOR(
            Set.of(
                    INSTRUCTOR_READ,
                    INSTRUCTOR_UPDATE,
                    INSTRUCTOR_DELETE,
                    INSTRUCTOR_CREATE
            )
    ),
    ADMIN(
            Set.of(
                    ADMIN_READ,
                    ADMIN_UPDATE,
                    ADMIN_DELETE,
                    ADMIN_CREATE
            )
    );

    @Getter
    private final Set<Permission> permissions;

    public List<SimpleGrantedAuthority> getAuthorities() {
        var authorities = getPermissions()
                .stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toList());
        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return authorities;
    }
}
