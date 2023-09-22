package com.renan.webstore.business

import com.renan.webstore.controller.auth.form.*
import com.renan.webstore.dto.UserDTO
import org.springframework.security.core.userdetails.UserDetailsService

interface AuthService: UserDetailsService {

    @Throws(BusinessException::class)
    fun login(form: LoginForm): UserDTO

    @Throws(BusinessException::class)
    fun recoverPassword(form: RecoverPasswordForm)

    @Throws(BusinessException::class)
    fun resetPasswordByCode(form: PasswordResetForm): UserDTO

    @Throws(BusinessException::class)
    fun registerUser(form: UserRegistrationForm)

    @Throws(BusinessException::class)
    fun activateUser(form: UserActivationForm): UserDTO

    @Throws(BusinessException::class)
    fun requestUserActivation(form: RequestUserActivationForm)

}