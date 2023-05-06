package com.renan.todo.service

/**
 * Service for sending e-mail for the users
 */
interface MailService {

    /**
     * Send email to the user (destination)
     */
    @Throws(BusinessException::class)
    fun sendEmail(destination: String, subject: String, message: String)

    /**
     * Send email to the users (destinationList)
     */
    @Throws(BusinessException::class)
    fun sendEmail(destinationList: List<String>, subject: String, message: String)

    /**
     * Send email to the user to reset the password
     */
    @Throws(BusinessException::class)
    fun sendNewPasswordEmail(destination: String, newPasswordCode: String)

    /**
     * Send email to the user to activate him on the system
     */
    @Throws(BusinessException::class)
    fun sendActivationEmail(destination: String, activationCode: String)

}