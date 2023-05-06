package com.renan.todo.service

/**
 * Services for user info update and password update
 */
interface UserService {

    /**
     * Update information about the user
     */
    @Throws(BusinessException::class)
    fun updateUser(id: Int?, name: String?, lastName: String?)

    /**
     * Update information about the user
     */
    @Throws(BusinessException::class)
    fun updateUserPassword(id: Int?, password: String?)

}