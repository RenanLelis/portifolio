package com.renan.webstore.controller.user.form

data class UserPasswordForm(
    val newPassword: String,
)

data class UserNameForm(
    val name: String,
    val lastName: String?,
)

data class AddressForm(
    val title: String?,
    val zipCode: String,
    val street: String,
    val number: String,
    val neighborhood: String?,
    val city: String,
    val state: String,
    val country: String = "Brasil",
    val default: Boolean = false
)

data class CardForm(
    val title: String?,
    val number: String,
    val name: String,
    val month: String,
    val year: String,
)