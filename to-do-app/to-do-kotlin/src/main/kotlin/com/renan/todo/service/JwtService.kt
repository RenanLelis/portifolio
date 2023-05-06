package com.renan.todo.service

import io.jsonwebtoken.Claims

/**
 * Service for jwt Operations - JSON Web Token
 */
interface JwtService {

    /**
     * Generate a JWT Token signed
     */
    fun generateJWT(
        idUser: Int?, idStatus: Int?, email: String?, firstName: String?,
        lastName: String?
    ): String?

    /**
     * Generate new JWT validating the old
     */
    @Throws(RuntimeException::class)
    fun refreshJWT(jwt: String?): String?

    /**
     * get the user id from the jwt token
     */
    @Throws(RuntimeException::class)
    fun getIdUser(jwt: String?): Int?

    /**
     * Validate the JWT, sign, values and if is expired
     */
    @Throws(RuntimeException::class)
    fun validateJWT(jwt: String?): Claims?

    /**
     * Validate the jwt token
     *
     * @param jwt - the token
     * @return - true if is valid
     * @throws RuntimeException - in case of errors
     */
    @Throws(RuntimeException::class)
    fun validateJWTToken(jwt: String?): Boolean?


}