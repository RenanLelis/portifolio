package com.renan.todo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * User Entity class
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "USER_TODO")
public class User {

    public static final Integer STATUS_ACTIVE = 1;
    public static final Integer STATUS_INACTIVE = 0;

    public static final Integer LENGTH_ACTIVATION_CODE = 6;
    public static final Integer LENGTH_NEW_PASSWORD_CODE = 6;

    public static final Integer MIN_LENGTH_PASSWORD = 6;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "FIRST_NAME", nullable = false)
    private String firstName;
    @Column(name = "LAST_NAME", nullable = true)
    private String lastName;
    @Column(name = "EMAIL", nullable = false, unique = true)
    private String email;
    @Column(name = "USER_PASSWORD", nullable = false)
    private String password;
    @Column(name = "ACTIVATION_CODE", nullable = true)
    private String activationCode;
    @Column(name = "NEW_PASSWORD_CODE", nullable = true)
    private String newPasswordCode;
    @Column(name = "USER_STATUS", nullable = false)
    private Integer userStatus;


}