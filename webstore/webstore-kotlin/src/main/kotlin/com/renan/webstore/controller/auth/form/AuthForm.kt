package com.renan.webstore.controller.auth.form

data class LoginForm (
    val email: String,
    val password: String
)

data class PasswordResetForm (
    val email: String,
    val password: String,
    val newPasswordCode: String
)

data class RecoverPasswordForm (
    val email: String,
)

data class RequestUserActivationForm (
    val email: String,
)

data class UserActivationForm (
    val email: String,
    val activationCode: String
)

data class UserRegistrationForm (
    val email: String,
    val password: String,
    val firstName: String,
    val lastName: String?
)