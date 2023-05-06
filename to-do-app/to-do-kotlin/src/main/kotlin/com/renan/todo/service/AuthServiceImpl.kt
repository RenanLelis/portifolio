package com.renan.todo.service

import com.renan.todo.dto.UserDTO
import com.renan.todo.dto.UserDTOMapper
import com.renan.todo.model.User
import com.renan.todo.persistence.UserRepository
import com.renan.todo.util.LENGTH_ACTIVATION_CODE
import com.renan.todo.util.LENGTH_NEW_PASSWORD_CODE
import com.renan.todo.util.MIN_LENGTH_PASSWORD
import com.renan.todo.util.STATUS_ACTIVE
import com.renan.todo.util.STATUS_INACTIVE
import com.renan.todo.util.getErrorMessageEmailAlreadyExists
import com.renan.todo.util.getErrorMessageInputValues
import com.renan.todo.util.getErrorMessageLogin
import com.renan.todo.util.getErrorMessageUserNotActive
import com.renan.todo.util.getErrorMessageUserNotFound
import org.springframework.stereotype.Service

/**
 * Services for user authentication, reset password, register and activation
 */
@Service
class AuthServiceImpl(
    val userRepository: UserRepository,
    val mailService: MailService,
    val userDTOMapper: UserDTOMapper,
    val utilService: UtilService,
    val taskListService: TaskListService
) : AuthService {

    /**
     * Makes user authentication
     */
    override fun login(email: String?, password: String?): UserDTO {
        if (!utilService.isMail(email) || password == null || password.length < MIN_LENGTH_PASSWORD)
            throw BusinessException(getErrorMessageInputValues(), BUSINESS_MESSAGE, AppErrorType.INVALID_INPUT)

        val userOptional = userRepository.findByEmail(email!!)
        if (userOptional.isEmpty || !comparePasswordWithHash(userOptional.get().password!!, password))
            throw BusinessException(getErrorMessageLogin(), BUSINESS_MESSAGE, AppErrorType.INVALID_INPUT)
        if (userOptional.get().userStatus == STATUS_INACTIVE)
            throw BusinessException(getErrorMessageUserNotActive(), BUSINESS_MESSAGE, AppErrorType.INVALID_INPUT)

        return userDTOMapper.apply(userOptional.get())
    }

    /**
     * Generate a code for password reset
     */
    override fun recoverPassword(email: String?) {
        if (!utilService.isMail(email))
            throw BusinessException(getErrorMessageInputValues(), BUSINESS_MESSAGE, AppErrorType.INVALID_INPUT)

        val userOptional = userRepository.findByEmail(email!!)
        if (userOptional.isEmpty)
            throw BusinessException(getErrorMessageUserNotFound(), BUSINESS_MESSAGE, AppErrorType.INVALID_INPUT)

        val user = userOptional.get()
        if (user.userStatus == STATUS_INACTIVE)
            throw BusinessException(getErrorMessageUserNotActive(), BUSINESS_MESSAGE, AppErrorType.INVALID_INPUT)

        val newPasswordCode = utilService.generateRandomString()
        user.newPasswordCode = newPasswordCode
        mailService.sendNewPasswordEmail(user.email!!, newPasswordCode)
        userRepository.save(user)
    }

    /**
     * Reset the password and make user authentication
     */
    override fun resetPasswordByCode(email: String?, newPassword: String?, newPasswordCode: String?): UserDTO {
        if (!validateResetPasswordInput(email!!, newPassword, newPasswordCode))
            throw BusinessException(
                getErrorMessageInputValues(),
                BUSINESS_MESSAGE,
                AppErrorType.INVALID_INPUT
            )

        val userOptional = userRepository.findByEmail(email)
        if (userOptional.isEmpty)
            throw BusinessException(
                getErrorMessageUserNotFound(),
                BUSINESS_MESSAGE,
                AppErrorType.INVALID_INPUT
            )

        val user = userOptional.get()
        if (user.newPasswordCode != newPasswordCode!!.trim { it <= ' ' })
            throw BusinessException(getErrorMessageInputValues(), BUSINESS_MESSAGE, AppErrorType.INVALID_INPUT)
        user.newPasswordCode = null
        user.userStatus = STATUS_ACTIVE
        user.activationCode = null
        user.password = utilService.generateHashString(newPassword!!.trim { it <= ' ' })
        userRepository.save(user)
        return userDTOMapper.apply(user)
    }

    /**
     * Create new user on the database
     */
    override fun registerUser(email: String?, password: String?, name: String?, lastName: String?) {
        if (!validateNewUserInput(email, password, name))
            throw BusinessException(
                getErrorMessageInputValues(),
                BUSINESS_MESSAGE,
                AppErrorType.INVALID_INPUT
            )

        val userOptional = userRepository.findByEmail(email!!)
        if (userOptional.isPresent)
            throw BusinessException(
                getErrorMessageEmailAlreadyExists(),
                BUSINESS_MESSAGE,
                AppErrorType.INVALID_INPUT
            )

        val user = User()
        user.id = null
        user.firstName = name!!.trim { it <= ' ' }
        user.lastName = lastName?.trim { it <= ' ' }
        user.email = email.trim { it <= ' ' }
        user.password = utilService.generateHashString(password!!.trim { it <= ' ' })
        user.activationCode = utilService.generateRandomString()
        user.userStatus = STATUS_INACTIVE
        user.newPasswordCode = null
        val newUser = userRepository.save(user)
        taskListService.createDefaultListForNewUser(newUser.id)
        mailService.sendActivationEmail(newUser.email!!, newUser.activationCode!!)
    }

    /**
     * Activate the user on the system
     */
    override fun activateUser(email: String?, activationCode: String?): UserDTO {
        if (!utilService.isMail(email) || activationCode == null || activationCode.trim { it <= ' ' }.length != LENGTH_ACTIVATION_CODE)
            throw BusinessException(
                getErrorMessageInputValues(),
                BUSINESS_MESSAGE,
                AppErrorType.INVALID_INPUT
            )

        val userOptional = userRepository.findByEmail(email!!.trim { it <= ' ' })
        if (userOptional.isEmpty)
            throw BusinessException(
                getErrorMessageUserNotFound(),
                BUSINESS_MESSAGE,
                AppErrorType.INVALID_INPUT
            )

        val user = userOptional.get()
        if (user.activationCode != activationCode.trim { it <= ' ' })
            throw BusinessException(
                getErrorMessageInputValues(),
                BUSINESS_MESSAGE,
                AppErrorType.INVALID_INPUT
            )

        user.activationCode = null
        user.userStatus = STATUS_ACTIVE
        userRepository.save(user)
        return userDTOMapper.apply(user)
    }

    /**
     * Generate activation code and send by email
     */
    override fun requestUserActivation(email: String?) {
        if (!utilService.isMail(email))
            throw BusinessException(
                getErrorMessageInputValues(),
                BUSINESS_MESSAGE,
                AppErrorType.INVALID_INPUT
            )

        val userOptional = userRepository.findByEmail(email!!.trim { it <= ' ' })
        if (userOptional.isEmpty)
            throw BusinessException(
                getErrorMessageUserNotFound(),
                BUSINESS_MESSAGE,
                AppErrorType.INVALID_INPUT
            )

        val user = userOptional.get()
        user.activationCode = utilService.generateRandomString()
        userRepository.save(user)
        mailService.sendActivationEmail(user.email!!, user.activationCode!!)
    }

    /**
     * Compare a string password with a hash to validate login operation
     */
    private fun comparePasswordWithHash(hash: String, password: String): Boolean {
        return hash == utilService.generateHashString(password)
    }

    /**
     * Validate the input for reset password operation
     */
    private fun validateResetPasswordInput(email: String?, newPassword: String?, newPasswordCode: String?) =
        utilService.isMail(email)
                && newPasswordCode != null && newPassword != null
                && newPasswordCode.trim { it <= ' ' }.length >= LENGTH_NEW_PASSWORD_CODE
                && newPassword.trim { it <= ' ' }.length >= MIN_LENGTH_PASSWORD


    /**
     * Validate the input for new user registration
     */
    private fun validateNewUserInput(email: String?, password: String?, name: String?) =
        utilService.isMail(email)
                && password != null && password.trim { it <= ' ' }.length >= MIN_LENGTH_PASSWORD
                && name != null && name.trim { it <= ' ' }.isNotEmpty()
}