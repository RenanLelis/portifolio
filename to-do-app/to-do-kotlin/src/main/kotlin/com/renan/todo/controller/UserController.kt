package com.renan.todo.controller

import com.renan.todo.controller.filter.AUTH
import com.renan.todo.controller.form.UserPasswordForm
import com.renan.todo.controller.form.UserProfileForm
import com.renan.todo.dto.ErrorDTO
import com.renan.todo.service.BusinessException
import com.renan.todo.service.JwtService
import com.renan.todo.service.UserService
import com.renan.todo.util.getErrorMessage
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController

/**
 * The controller for user profile and password update
 */
@RestController
class UserController(val userService: UserService, val jwtService: JwtService) {

    /**
     * Update information about the user
     */
    @PutMapping(value = ["/api/user/profile"], consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun updateUser(@RequestHeader(AUTH) jwt: String?, @RequestBody form: UserProfileForm) {
        userService.updateUser(jwtService.getIdUser(jwt),
            form.firstName.trim { it <= ' ' },
            if (form.lastName != null) form.lastName.trim { it <= ' ' } else null)
    }

    /**
     * Update user password
     */
    @PutMapping(value = ["/api/user/password"], consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun updateUserPassword(@RequestHeader(AUTH) jwt: String?, @RequestBody form: UserPasswordForm) {
        userService.updateUserPassword(jwtService.getIdUser(jwt), form.password.trim { it <= ' ' })
    }

}