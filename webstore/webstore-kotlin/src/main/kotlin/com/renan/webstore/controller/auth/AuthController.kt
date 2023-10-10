package com.renan.webstore.controller.auth

import com.renan.webstore.business.AuthService
import com.renan.webstore.controller.auth.form.*
import com.renan.webstore.dto.UserDTO
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/public/auth")
class AuthController(
    val authService: AuthService
) {

    @PostMapping("/login")
    fun login(@RequestBody form: LoginForm): UserDTO = authService.login(form)

    @PostMapping("/passwordrecover")
    fun recoverPassword(@RequestBody form: RecoverPasswordForm) = authService.recoverPassword(form)


    @PostMapping("/passwordreset")
    fun resetPasswordByCode(@RequestBody form: PasswordResetForm): UserDTO = authService.resetPasswordByCode(form)

    @PostMapping("/userregistration")
    @ResponseStatus(HttpStatus.CREATED)
    fun registerNewUser(@RequestBody form: UserRegistrationForm) = authService.registerUser(form)

    @PostMapping("/useractivation")
    fun activateUser(@RequestBody form: UserActivationForm): UserDTO = authService.activateUser(form)

    @PostMapping("/useractivationrequest")
    fun requestUserActivation(@RequestBody form: RequestUserActivationForm) = authService.requestUserActivation(form)

}