package com.renan.todo.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

/**
 * User Entity class
 */
@Entity
@Table(name = "USER_TODO")
class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    var id: Int? = null

    @Column(name = "FIRST_NAME", nullable = false)
    var firstName: String? = null

    @Column(name = "LAST_NAME", nullable = true)
    var lastName: String? = null

    @Column(name = "EMAIL", nullable = false, unique = true)
    var email: String? = null

    @Column(name = "USER_PASSWORD", nullable = false)
    var password: String? = null

    @Column(name = "ACTIVATION_CODE", nullable = true)
    var activationCode: String? = null

    @Column(name = "NEW_PASSWORD_CODE", nullable = true)
    var newPasswordCode: String? = null

    @Column(name = "USER_STATUS", nullable = false)
    var userStatus: Int? = null
}