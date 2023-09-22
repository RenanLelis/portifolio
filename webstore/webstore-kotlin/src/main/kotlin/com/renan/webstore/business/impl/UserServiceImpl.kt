package com.renan.webstore.business.impl

import com.renan.webstore.business.AppErrorType
import com.renan.webstore.business.BusinessException
import com.renan.webstore.business.UserService
import com.renan.webstore.business.validator.Validator
import com.renan.webstore.controller.user.form.UserNameForm
import com.renan.webstore.controller.user.form.UserPasswordForm
import com.renan.webstore.persistence.dao.UserDAO
import com.renan.webstore.util.getErrorMessageInputValues
import com.renan.webstore.util.getErrorMessageUserNotFound
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserServiceImpl(
    val userDAO: UserDAO,
    val validator: Validator,
    val passwordEncoder: PasswordEncoder
) : UserService {

    override fun updateUserPassword(form: UserPasswordForm, userId: Int) {
        if (!validator.isValidPassword(form.newPassword)) throw BusinessException(
            message = getErrorMessageInputValues(),
            businessMessage = true,
            errorType = AppErrorType.INVALID_INPUT
        )
        val optUser = userDAO.findById(userId)
        if (optUser.isEmpty) throw BusinessException(
            message = getErrorMessageUserNotFound(),
            businessMessage = true,
            errorType = AppErrorType.INVALID_INPUT
        )
        val user = optUser.get()
        user.userPassword = passwordEncoder.encode(form.newPassword)
        userDAO.saveUser(user)
    }

    override fun updateUserName(form: UserNameForm, userId: Int) {
        if (form.name.isBlank()) throw BusinessException(
            message = getErrorMessageInputValues(),
            businessMessage = true,
            errorType = AppErrorType.INVALID_INPUT
        )
        val optUser = userDAO.findById(userId)
        if (optUser.isEmpty) throw BusinessException(
            message = getErrorMessageUserNotFound(),
            businessMessage = true,
            errorType = AppErrorType.INVALID_INPUT
        )
        val user = optUser.get()
        user.firstName = form.name.trim()
        user.lastName = form.lastName?.trim()
        userDAO.saveUser(user)
    }

}