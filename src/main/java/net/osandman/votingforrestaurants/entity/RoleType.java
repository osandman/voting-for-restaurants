package net.osandman.votingforrestaurants.entity;

import org.springframework.security.core.GrantedAuthority;

public enum RoleType implements GrantedAuthority {
    ADMIN,
    REGULAR,
    GUEST;

    @Override
    public String getAuthority() {
        return "ROLE_" + name();
    }
}