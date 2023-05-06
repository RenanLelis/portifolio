package com.renan.todo.service

import com.renan.todo.dto.UserDTO

/**
 * Services for user authentication, reset password, register and activation
 */
interface AuthService {

    /**
     * Makes user authentication
     */
    @Throws(BusinessException::class)
    fun login(email: String?, password: String?): UserDTO

    /**
     * Generate a code for password reset
     */
    @Throws(BusinessException::class)
    fun recoverPassword(email: String?)

    /**
     * Reset the password and make user authentication
     */
    @Throws(BusinessException::class)
    fun resetPasswordByCode(email: String?, newPassword: String?, newPasswordCode: String?): UserDTO

    /**
     * Create new user on the database
     */
    @Throws(BusinessException::class)
    fun registerUser(email: String?, password: String?, name: String?, lastName: String?)

    /**
     * Activate the user on the system
     */
    @Throws(BusinessException::class)
    fun activateUser(email: String?, activationCode: String?): UserDTO

    /**
     * Generate activation code and send by email
     */
    @Throws(BusinessException::class)
    fun requestUserActivation(email: String?)
}