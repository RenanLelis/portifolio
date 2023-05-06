package com.renan.todo.util

import io.jsonwebtoken.SignatureAlgorithm

const val STATUS_ACTIVE = 1
const val STATUS_INACTIVE = 0

const val LENGTH_ACTIVATION_CODE = 6
const val LENGTH_NEW_PASSWORD_CODE = 6

const val MIN_LENGTH_PASSWORD = 6

const val STATUS_INCOMPLETE = 0
const val STATUS_COMPLETE = 1

const val MSE01 = "MSE01" // An error occurred on the operation.
const val MSE02 = "MSE02" // Error on values properties.
const val MSE03 = "MSE03" // Invalid Token.
const val MSE04 = "MSE04" // Invalid User or Password.
const val MSE05 = "MSE05" // E-mail already exists.
const val MSE06 = "MSE06" // E-mail not found.
const val MSE07 = "MSE07" // User not activated.
const val MSE08 = "MSE08" // Cannot delete only list of the user

fun getErrorMessage() = MSE01
fun getErrorMessageInputValues() = MSE02
fun getErrorMessageToken() = MSE03
fun getErrorMessageLogin() = MSE04
fun getErrorMessageEmailAlreadyExists() = MSE05
fun getErrorMessageUserNotFound() = MSE06
fun getErrorMessageUserNotActive() = MSE07
fun getErrorMessageDeleteOnlyListOfUser() = MSE08


const val API_KEY = "07J0PpNnASqADC9LVooxIVREQs6jUq0yyvVpGVQXJkAPtwLZ7cMQSiBbT9aSv8O1n7q3YKbW2l1niOWCbcz3ng=="
const val CLAIMS_ID_USER = "USER"
const val CLAIMS_EMAIL = "EMAIL"
const val CLAIMS_ID_STATUS = "STATUS"
const val CLAIMS_AUTHORIZED = "AUTHORIZED"
const val CLAIMS_FIRST_NAME = "FIRST_NAME"
const val CLAIMS_LAST_NAME = "LAST_NAME"
const val TIMEOUT = 2 * 60 * 60 * 1000
val SIGNATURE_ALGORITHM = SignatureAlgorithm.HS256