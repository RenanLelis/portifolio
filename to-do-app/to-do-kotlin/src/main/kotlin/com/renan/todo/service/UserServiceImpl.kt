package com.renan.todo.service

import com.renan.todo.persistence.UserRepository
import com.renan.todo.util.MIN_LENGTH_PASSWORD
import com.renan.todo.util.getErrorMessageInputValues
import org.springframework.stereotype.Service

/**
 * Services for user info update and password update
 */
@Service
class UserServiceImpl(val userRepository: UserRepository, val utilService: UtilService) : UserService {

    /**
     * Update information about the user
     */
    override fun updateUser(id: Int?, name: String?, lastName: String?) {
        if (!validateUpdateUserInput(id, name)) {
            throw BusinessException(
                getErrorMessageInputValues(),
                BUSINESS_MESSAGE,
                AppErrorType.INVALID_INPUT
            )
        }
        userRepository.updateUserData(id!!, name!!.trim { it <= ' ' }, lastName?.trim { it <= ' ' })
    }

    /**
     * Update user password
     */
    override fun updateUserPassword(id: Int?, password: String?) {
        if (!validateUserUpdatePasswordInput(id, password)) {
            throw BusinessException(
                getErrorMessageInputValues(),
                BUSINESS_MESSAGE,
                AppErrorType.INVALID_INPUT
            )
        }
        userRepository.updateUserPassword(id!!, utilService.generateHashString(password!!.trim { it <= ' ' }))
    }

    /**
     * Validate the input data for user update operation
     */
    private fun validateUpdateUserInput(id: Int?, name: String?) =
        (id != null && id > 0 && name != null && name.trim { it <= ' ' }.isNotEmpty())

    /**
     * Validate the input data for user password update operation
     */
    private fun validateUserUpdatePasswordInput(id: Int?, password: String?) =
        (id != null && id > 0 && password != null && password.trim { it <= ' ' }.length > MIN_LENGTH_PASSWORD)

}