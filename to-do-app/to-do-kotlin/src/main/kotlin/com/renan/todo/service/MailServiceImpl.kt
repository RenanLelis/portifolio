package com.renan.todo.service

import com.renan.todo.util.getErrorMessage
import jakarta.mail.Authenticator
import jakarta.mail.Message
import jakarta.mail.MessagingException
import jakarta.mail.PasswordAuthentication
import jakarta.mail.Session
import jakarta.mail.Transport
import jakarta.mail.internet.InternetAddress
import jakarta.mail.internet.MimeMessage
import java.util.*
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

const val NEW_PASSWORD_SUBJECT = "Change your password - TODO-APP"
const val USER_ACTIVATION_SUBJECT = "Activate your user - TODO-APP"

/**
 * Service for sending e-mail for the users
 */
@Service
class MailServiceImpl : MailService {

    @Value("\${MAIL_FROM}")
    private var emailFrom: String? = null

    @Value("\${MAIL_PASSWORD}")
    private var password: String? = null

    @Value("\${MAIL_SMTP_HOST}")
    private var smtpHost: String? = null

    @Value("\${MAIL_SMTP_PORT}")
    private var smtpPort: String? = null

    /**
     * Send email to the user (destination)
     */
    @Throws(BusinessException::class)
    override fun sendEmail(destination: String, subject: String, message: String) {
        val session = getSession()
        val mimeMessage = MimeMessage(session)
        try {
            mimeMessage.setFrom(InternetAddress(emailFrom))
            val toUser: Array<out InternetAddress>? = InternetAddress.parse(destination)
            mimeMessage.setRecipients(Message.RecipientType.TO, toUser)
            mimeMessage.subject = subject
            mimeMessage.setText(message, "utf-8", "html")
            Transport.send(mimeMessage)
        } catch (e: MessagingException) {
            throw BusinessException(getErrorMessage(), true, AppErrorType.INTERN_ERROR)
        }
    }

    /**
     * Send email to the users (destinationList)
     */
    @Throws(BusinessException::class)
    override fun sendEmail(destinationList: List<String>, subject: String, message: String) {
        val dest = StringBuilder()
        for (destination in destinationList) {
            dest.append(destination)
            dest.append(", ")
        }
        sendEmail(dest.toString(), subject, message)
    }

    /**
     * Send email to the user to reset the password
     */
    @Throws(BusinessException::class)
    override fun sendNewPasswordEmail(destination: String, newPasswordCode: String) =
        sendEmail(destination, NEW_PASSWORD_SUBJECT, generateEmailNewPassword(newPasswordCode))

    /**
     * Send email to the user to activate him on the system
     */
    @Throws(BusinessException::class)
    override fun sendActivationEmail(destination: String, activationCode: String) =
        sendEmail(destination, USER_ACTIVATION_SUBJECT, generateEmailUserActivation(activationCode))

    /**
     * generate the email message with the new code to change password
     */
    private fun generateEmailNewPassword(newPasswordCode: String): String {
        return ("<div style=\" text-align:center; width:450px; margin:10px auto; border-radius:10px;\">"
                + "<h1 style=\"color:#555555; font-size: 20px;\">TODO-APP</h1>"
                + "<p style=\"color:#555555; font-size: 16px; padding:10px; \">To generate a new password use the code below</p>"
                + "<p style=\"color:#555555; font-size: 16px;\"> " + newPasswordCode + " </p>" + "</div>")
    }

    /**
     * generate the email message with the new code to activate the user
     */
    private fun generateEmailUserActivation(activationCode: String): String {
        return ("<div style=\" text-align:center; width:450px; margin:10px auto; border-radius:10px;\">"
                + "<h1 style=\"color:#555555; font-size: 20px;\">TODO-APP</h1>"
                + "<p style=\"color:#555555; font-size: 16px; padding:10px; \">To activate your user use the code below</p>"
                + "<p style=\"color:#555555; font-size: 16px;\"> " + activationCode + " </p>" + "</div>")
    }

    /**
     * Create session for email connection
     */
    private fun getSession(): Session {
        return Session.getInstance(getProperties(), object : Authenticator() {
            override fun getPasswordAuthentication(): PasswordAuthentication {
                return PasswordAuthentication(emailFrom, password)
            }
        })
    }

    /**
     * Load properties for connection with the smtp server
     */
    private fun getProperties(): Properties {
        val props = Properties()
        props["mail.smtp.host"] = smtpHost
        props["mail.smtp.socketFactory.port"] = smtpPort
        props["mail.smtp.socketFactory.class"] = "javax.net.ssl.SSLSocketFactory"
        props["mail.smtp.auth"] = "true"
        props["mail.smtp.port"] = smtpPort
        return props
    }

}