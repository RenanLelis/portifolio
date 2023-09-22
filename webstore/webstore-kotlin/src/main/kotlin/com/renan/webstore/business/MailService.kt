package com.renan.webstore.business

interface MailService {

    @Throws(BusinessException::class)
    fun sendEmail(destination: String, subject: String, message: String)

    @Throws(BusinessException::class)
    fun sendEmail(destinationList: List<String>, subject: String, message: String)

    @Throws(BusinessException::class)
    fun sendNewPasswordEmail(destination: String, newPasswordCode: String)

    @Throws(BusinessException::class)
    fun sendActivationEmail(destination: String, activationCode: String)

}