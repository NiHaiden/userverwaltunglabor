package tech.niklas.userverwaltung.auth;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

import static tech.niklas.userverwaltung.auth.Permissions.USER_READ;
import static tech.niklas.userverwaltung.auth.Permissions.USER_WRITE;


public enum Roles {
    USER(Set.of(USER_READ)),
    ADMIN(Set.of(USER_READ, USER_WRITE));

    private final Set<Permissions> permissions;

    Roles(Set<Permissions> permissions) {
        this.permissions = permissions;
    }

    public Set<SimpleGrantedAuthority> getAuthorities() {
        var authorities =
                permissions.
                        stream().
                        map(Permissions::getPermission).
                        map(SimpleGrantedAuthority::new).
                        collect(Collectors.toSet());
        authorities.add(new SimpleGrantedAuthority("ROLE_" + this));
        return authorities;
    }
}
