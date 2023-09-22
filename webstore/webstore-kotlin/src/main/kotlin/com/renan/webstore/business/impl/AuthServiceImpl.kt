package com.renan.webstore.business.impl

import com.renan.webstore.business.AppErrorType
import com.renan.webstore.business.AuthService
import com.renan.webstore.business.BusinessException
import com.renan.webstore.business.MailService
import com.renan.webstore.business.validator.Validator
import com.renan.webstore.controller.auth.form.*
import com.renan.webstore.dto.UserDTO
import com.renan.webstore.dto.mapper.UserDTOMapper
import com.renan.webstore.model.*
import com.renan.webstore.persistence.dao.UserDAO
import com.renan.webstore.util.getErrorMessageEmailAlreadyExists
import com.renan.webstore.util.getErrorMessageInputValues
import com.renan.webstore.util.getErrorMessageLogin
import com.renan.webstore.util.getErrorMessageUserNotFound
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthServiceImpl(
    val userDAO: UserDAO,
    val mailService: MailService,
    val validator: Validator,
    val passwordEncoder: PasswordEncoder,
    val userDTOMapper: UserDTOMapper
): AuthService {

    @Throws(BusinessException::class)
    override fun login(form: LoginForm): UserDTO {
        validateLoginForm(form)
        val user = userDAO.findByEmail(form.email.trim().lowercase())
        if (user?.id == null || user.id <= 0) {
            throw BusinessException(
                message = getErrorMessageUserNotFound(),
                businessMessage = true,
                errorType = AppErrorType.INVALID_INPUT
            )
        }
        if (!passwordEncoder.matches(form.password.trim(), user.userPassword)) {
            throw BusinessException(
                message = getErrorMessageLogin(),
                businessMessage = true,
                errorType = AppErrorType.INVALID_INPUT
            )
        }
        return userDTOMapper.apply(user);
    }

    @Throws(BusinessException::class)
    override fun recoverPassword(form: RecoverPasswordForm) {
        if (!validator.isMail(form.email)) throw BusinessException(
            message = getErrorMessageInputValues(),
            businessMessage = true,
            errorType = AppErrorType.INVALID_INPUT
        )
        val user = userDAO.findByEmail(form.email.trim().lowercase())
        if (user?.id == null || user.id <= 0) {
            throw BusinessException(
                message = getErrorMessageUserNotFound(),
                businessMessage = true,
                errorType = AppErrorType.INVALID_INPUT
            )
        }
        user.newPasswordCode = generateNewPasswordCode()
        userDAO.updateUser(user)
        mailService.sendNewPasswordEmail(user.email, user.newPasswordCode!!)
    }

    @Throws(BusinessException::class)
    override fun resetPasswordByCode(form: PasswordResetForm): UserDTO {
        if (!validator.isMail(form.email) || !validator.isValidNewPasswordCode(form.newPasswordCode)) throw BusinessException(
            message = getErrorMessageInputValues(),
            businessMessage = true,
            errorType = AppErrorType.INVALID_INPUT
        )
        val user = userDAO.findByEmail(form.email.trim().lowercase())
        if (user?.id == null || user.id <= 0) {
            throw BusinessException(
                message = getErrorMessageUserNotFound(),
                businessMessage = true,
                errorType = AppErrorType.INVALID_INPUT
            )
        }
        if (user.newPasswordCode.isNullOrBlank() || !user.newPasswordCode!!.trim()
                .equals(form.newPasswordCode.trim())
        ) throw BusinessException(
            message = getErrorMessageInputValues(),
            businessMessage = true,
            errorType = AppErrorType.INVALID_INPUT
        )
        user.newPasswordCode = null
        user.userStatus = STATUS_ACTIVE
        userDAO.updateUser(user)
        return userDTOMapper.apply(user)
    }

    @Throws(BusinessException::class)
    override fun registerUser(form: UserRegistrationForm) {
        validateNewUserForm(form)
        var user = userDAO.findByEmail(form.email.trim().lowercase())
        if (user?.id != null && user.id!! > 0) {
            throw BusinessException(
                message = getErrorMessageEmailAlreadyExists(),
                businessMessage = true,
                errorType = AppErrorType.INVALID_INPUT
            )
        }
        val activationCode = generateActivationCode()
        user = AppUser(
            firstName = form.firstName.trim(),
            lastName = form.lastName?.trim(),
            email = form.email,
            userPassword = passwordEncoder.encode(form.password.trim()),
            roles = ROLE_PUBLIC,
            activationCode = activationCode,
            userStatus = STATUS_INACTIVE,
            id = null,
            newPasswordCode = null
        )
        userDAO.saveUser(user)
        mailService.sendActivationEmail(user.email, activationCode)
    }

    @Throws(BusinessException::class)
    override fun activateUser(form: UserActivationForm): UserDTO {
        if (!validator.isMail(form.email) || !validator.isValidActivationCode(form.activationCode)) throw BusinessException(
            message = getErrorMessageInputValues(),
            businessMessage = true,
            errorType = AppErrorType.INVALID_INPUT
        )
        val user = userDAO.findByEmail(form.email.trim().lowercase())
        if (user?.id == null || user.id <= 0) {
            throw BusinessException(
                message = getErrorMessageUserNotFound(),
                businessMessage = true,
                errorType = AppErrorType.INVALID_INPUT
            )
        }
        if (user.activationCode.isNullOrBlank() || !user.activationCode!!.trim().equals(form.activationCode.trim()))
            throw BusinessException(
                message = getErrorMessageInputValues(),
                businessMessage = true,
                errorType = AppErrorType.INVALID_INPUT
            )
        user.userStatus = STATUS_ACTIVE
        user.activationCode = null
        userDAO.updateUser(user)
        return userDTOMapper.apply(user)
    }

    @Throws(BusinessException::class)
    override fun requestUserActivation(form: RequestUserActivationForm) {
        if (!validator.isMail(form.email)) throw BusinessException(
            message = getErrorMessageInputValues(),
            businessMessage = true,
            errorType = AppErrorType.INVALID_INPUT
        )
        val user = userDAO.findByEmail(form.email.trim().lowercase())
        if (user?.id == null || user.id <= 0) {
            throw BusinessException(
                message = getErrorMessageUserNotFound(),
                businessMessage = true,
                errorType = AppErrorType.INVALID_INPUT
            )
        }
        user.activationCode = generateActivationCode()
        userDAO.updateUser(user)
        mailService.sendActivationEmail(user.email, user.activationCode!!)
    }

    override fun loadUserByUsername(username: String?): UserDetails {
        if (username.isNullOrBlank() || !validator.isMail(username)) throw BusinessException(
            message = getErrorMessageLogin(),
            businessMessage = true,
            errorType = AppErrorType.INVALID_INPUT
        )
        return userDAO.findByEmail(username)
            ?: throw BusinessException(
                message = getErrorMessageUserNotFound(),
                businessMessage = true,
                errorType = AppErrorType.INVALID_INPUT
            )
    }

    @Throws(BusinessException::class)
    private fun validateLoginForm(form: LoginForm) {
        if (!validator.isMail(form.email) || !validator.isValidPassword(form.password)) {
            throw BusinessException(
                message = getErrorMessageInputValues(),
                businessMessage = true,
                errorType = AppErrorType.INVALID_INPUT
            )
        }
    }

    @Throws(BusinessException::class)
    private fun validateNewUserForm(form: UserRegistrationForm) {
        if (!validator.isMail(form.email) || !validator.isValidPassword(form.password)
            || form.firstName.isEmpty()
        ) {
            throw BusinessException(
                message = getErrorMessageInputValues(),
                businessMessage = true,
                errorType = AppErrorType.INVALID_INPUT
            )
        }
    }

}