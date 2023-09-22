package com.renan.webstore.util

private const val MSE01 = "MSE01" // An error occurred on the operation.
private const val MSE02 = "MSE02" // Error on values properties.
private const val MSE03 = "MSE03" // Invalid Token.
private const val MSE04 = "MSE04" // Invalid User or Password.
private const val MSE05 = "MSE05" // E-mail already exists.
private const val MSE06 = "MSE06" // E-mail not found.
private const val MSE07 = "MSE07" // User not activated.
private const val MSE08 = "MSE08" // User does not have the required privileges.

fun getErrorMessage() =  MSE01
fun getErrorMessageInputValues() = MSE02
fun getErrorMessageToken() = MSE03
fun getErrorMessageLogin() = MSE04
fun getErrorMessageEmailAlreadyExists() = MSE05
fun getErrorMessageUserNotFound() = MSE06
fun getErrorMessageUserNotActive() = MSE07
fun getErrorMessageDoNotHavePrivilege() = MSE08