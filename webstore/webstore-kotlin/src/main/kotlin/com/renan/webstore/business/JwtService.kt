package com.renan.webstore.business

import io.jsonwebtoken.Claims
import io.jsonwebtoken.SignatureAlgorithm

const val AUTH = "AUTHORIZATION"

const val API_KEY = "07J0PpNnASqADC9LVooxIVREQs6jUq0yyvVpGVQXJkAPtwLZ7cMQSiBbT9aSv8O1n7q3YKbW2l1niOWCbcz3ng=="
const val CLAIMS_ID_USER = "USER"
const val CLAIMS_EMAIL = "EMAIL"
const val CLAIMS_STATUS = "STATUS"
const val CLAIMS_AUTHORIZED = "AUTHORIZED"
const val CLAIMS_FIRST_NAME = "FIRST_NAME"
const val CLAIMS_LAST_NAME = "LAST_NAME"
const val CLAIMS_ROLES = "ROLES"
const val TIMEOUT = 2 * 60 * 60 * 1000
val SIGNATURE_ALGORITHM: SignatureAlgorithm = SignatureAlgorithm.HS256

interface JwtService {

    @Throws(RuntimeException::class)
    fun generateJWT(
        idUser: Int,
        active: Boolean,
        email: String,
        firstName: String,
        lastName: String?,
        roles: List<String>
    ): String

    @Throws(RuntimeException::class)
    fun refreshJWT(jwt: String): String

    @Throws(RuntimeException::class)
    fun getIdUser(jwt: String): Int

    @Throws(RuntimeException::class)
    fun validateJWT(jwt: String): Claims

}