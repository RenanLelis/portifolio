package com.renan.todo.controller

import com.renan.todo.controller.form.LoginForm
import com.renan.todo.controller.form.PasswordResetForm
import com.renan.todo.controller.form.RecoverPasswordForm
import com.renan.todo.controller.form.RequestUserActivationForm
import com.renan.todo.controller.form.UserActivationForm
import com.renan.todo.controller.form.UserRegistrationForm
import com.renan.todo.dto.ErrorDTO
import com.renan.todo.dto.UserDTO
import com.renan.todo.service.AuthService
import com.renan.todo.service.BusinessException
import com.renan.todo.util.getErrorMessage
import java.util.*
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

/**
 * The controller for authentication, forget password and register operations
 */
@RestController
class AuthController(val authService: AuthService) {

    /**
     * Rest Controller function for login
     */
    @PostMapping(
        value = ["/api/auth/login"],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun login(@RequestBody form: LoginForm) = authService.login(form.email, form.password)

    /**
     * Generate a code to recover password and send to e-mail
     */
    @PostMapping(value = ["/api/auth/recoverpassword"], consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun recoverPassword(@RequestBody form: RecoverPasswordForm) {
        authService.recoverPassword(form.email.trim { it <= ' ' }
            .lowercase(Locale.getDefault()))
    }
//    authService.recoverPassword(if (form.email != null) form.email.trim { it <= ' ' }
//    .lowercase(Locale.getDefault()) else null)

    /**
     * Reset the password with the code send to e-mail
     */
    @PostMapping(
        value = ["/api/auth/passwordreset"],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun passwordReset(@RequestBody form: PasswordResetForm) = authService.resetPasswordByCode(
            form.email.trim { it <= ' ' }.lowercase(Locale.getDefault()),
            form.password.trim { it <= ' ' },
            form.newPasswordCode.trim { it <= ' ' })


    /**
     * Register a new user on the database
     */
    @PostMapping(value = ["/api/auth/userregistration"], consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun registerNewUser(@RequestBody form: UserRegistrationForm) {
        authService.registerUser(
            form.email.trim { it <= ' ' }.lowercase(Locale.getDefault()),
            form.password.trim { it <= ' ' },
            form.firstName.trim { it <= ' ' },
            if (form.lastName != null) form.lastName.trim { it <= ' ' } else null)
    }

    /**
     * activate user on the system
     */
    @PostMapping(
        value = ["/api/auth/useractivation"],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun activateUser(@RequestBody form: UserActivationForm): UserDTO {
        return authService.activateUser(
            form.email.trim { it <= ' ' }.lowercase(Locale.getDefault()),
            form.activationCode.trim { it <= ' ' })
    }

    /**
     * request for user activation on the system
     */
    @PostMapping(value = ["/api/auth/useractivationrequest"], consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun requestUserActivation(@RequestBody form: RequestUserActivationForm) {
        authService.requestUserActivation(form.email.trim { it <= ' ' }
            .lowercase(Locale.getDefault()))
    }

}