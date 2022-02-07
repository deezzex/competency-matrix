package com.nerdysoft.competencymatrix.entity.enums;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    USER,ADMIN,COMPETENCE_MANAGER,EVALUATOR;

    @Override
    public String getAuthority() {
        return name();
    }
}
