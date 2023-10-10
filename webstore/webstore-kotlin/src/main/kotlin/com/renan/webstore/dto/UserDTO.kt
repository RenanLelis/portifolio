package com.renan.webstore.dto

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include

@JsonInclude(Include.NON_NULL)
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
