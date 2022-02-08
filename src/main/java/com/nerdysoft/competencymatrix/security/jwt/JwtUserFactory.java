package com.nerdysoft.competencymatrix.security.jwt;

import com.nerdysoft.competencymatrix.entity.User;
import com.nerdysoft.competencymatrix.entity.privilege.Permission;
import com.nerdysoft.competencymatrix.entity.privilege.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;

public final class JwtUserFactory {

    public JwtUserFactory() {
    }

    public static JwtUser create(User user) {
        return new JwtUser(
                user.getId(),
                user.getUsername(),
                user.getFirstName(),
                user.getLastName(),
                user.getPassword(),
                true,
                mapToGrantedAuthorities(new ArrayList<>(user.getRoles())));
    }

    private static List<GrantedAuthority> mapToGrantedAuthorities(List<Role> userRoles) {

        List<GrantedAuthority> authorities = new ArrayList<>();
        List<Permission> permissions = new ArrayList<>();

        for (Role role : userRoles) {
            permissions.addAll(role.getPermissions());
        }

        for (Permission permission : permissions) {
            authorities.add(new SimpleGrantedAuthority(permission.getName()));
        }

        return authorities;
    }
}
