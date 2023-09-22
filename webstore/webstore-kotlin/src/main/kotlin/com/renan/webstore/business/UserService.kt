package com.renan.webstore.business

import com.renan.webstore.controller.user.form.UserNameForm
import com.renan.webstore.controller.user.form.UserPasswordForm

interface UserService {

    @Throws(BusinessException::class)
    fun updateUserPassword(form: UserPasswordForm, userId: Int)

    @Throws(BusinessException::class)
    fun updateUserName(form: UserNameForm, userId: Int)

}