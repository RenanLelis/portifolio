package com.renan.webstore.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.Arrays;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "USER_WEB_STORE")
public class AppUser {
    
    public static final Integer LENGTH_ACTIVATION_CODE = 6;
    public static final Integer LENGTH_NEW_PASSWORD_CODE = 6;
    
    public static final Integer MIN_LENGTH_PASSWORD = 6;
    
    public static final String ROLE_PUBLIC = "PUBLIC";
    public static final String ROLE_ADMIN = "ADMIN";
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "FIRST_NAME", nullable = false)
    private String firstName;
    @Column(name = "LAST_NAME")
    private String lastName;
    @Column(name = "EMAIL", nullable = false, unique = true)
    private String email;
    @Column(name = "USER_PASSWORD", nullable = false)
    private String password;
    @Column(name = "ACTIVATION_CODE")
    private String activationCode;
    @Column(name = "NEW_PASSWORD_CODE")
    private String newPasswordCode;
    @Column(name = "USER_STATUS")
    private boolean active;
    @Column(name = "USER_ROLES", nullable = false)
    private String roles;
    
    public List<String> buildRoles(){
        if (roles == null || roles.isEmpty()) return null;
        return Arrays.stream(roles.split(",")).toList();
    }
    
    public String buildFieldRoles(List<String> listRoles) {
        if (listRoles == null || listRoles.isEmpty()) return null;
        if (listRoles.size() == 1) return listRoles.get(0);
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < listRoles.size(); i++) {
            String role = listRoles.get(i);
            builder.append(role);
            if (i < listRoles.size() -1) builder.append(",");
        }
        return builder.toString();
    }

}
