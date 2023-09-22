package com.renan.webstore.dto

data class UserDTO(
    val email: String,
    val jwt: String,
    val id: Int,
    val active: Boolean,
    val expiresIn: Int,
    val name: String,
    val lastName: String?,
    val roles: List<String>?
)
