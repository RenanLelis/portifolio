package com.renan.webstore.controller.user

import com.renan.webstore.business.JwtService
import com.renan.webstore.business.UserService
import com.renan.webstore.controller.user.form.UserNameForm
import com.renan.webstore.controller.user.form.UserPasswordForm
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/user/info")
class UserInfoController(
    val userService: UserService,
    val jwtService: JwtService
) {

    @PutMapping("/password")
    fun updateUserPassword(
        @RequestBody form: UserPasswordForm,
        @RequestHeader("Authorization") authHeader: String
    ) = userService.updateUserPassword(form, jwtService.getIdUser(authHeader))

    @PutMapping("/userinfo")
    fun updateUserInfo(
        @RequestBody form: UserNameForm,
        @RequestHeader("Authorization") authHeader: String
    ) = userService.updateUserName(form, jwtService.getIdUser(authHeader))



}