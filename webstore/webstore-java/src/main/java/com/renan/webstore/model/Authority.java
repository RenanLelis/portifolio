package com.renan.webstore.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@AllArgsConstructor
@NoArgsConstructor
public class Authority implements GrantedAuthority {
    
    private String authority;
    
    @Override
    public String getAuthority() {
        return authority;
    }
}
