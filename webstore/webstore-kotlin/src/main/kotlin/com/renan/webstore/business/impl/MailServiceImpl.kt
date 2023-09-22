package com.renan.webstore.business.impl

import com.renan.webstore.business.AppErrorType
import com.renan.webstore.business.BusinessException
import com.renan.webstore.business.MailService
import com.renan.webstore.util.getErrorMessage
import jakarta.mail.Authenticator
import jakarta.mail.Message
import jakarta.mail.MessagingException
import jakarta.mail.PasswordAuthentication
import jakarta.mail.Session
import jakarta.mail.Transport
import jakarta.mail.internet.InternetAddress
import jakarta.mail.internet.MimeMessage
import org.springframework.core.env.Environment
import org.springframework.stereotype.Service
import java.util.*

const val NEW_PASSWORD_SUBJECT = "Change your password - Webstore"
const val USER_ACTIVATION_SUBJECT = "Activate your user - Webstore"

@Service
class MailServiceImpl(
    val environment: Environment
) : MailService {

    private val MAIL_FROM = "MAIL_FROM"

    @Throws(BusinessException::class)
    override fun sendEmail(destination: String, subject: String, message: String) {
        val emailFrom = environment.getProperty(MAIL_FROM)
        val session: Session = getSession()
        val mimeMessage = MimeMessage(session)
        try {
            assert(emailFrom != null)
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

    @Throws(BusinessException::class)
    override fun sendEmail(destinationList: List<String>, subject: String, message: String) {
        val dest = StringBuilder()
        for (destination in destinationList) {
            dest.append(destination)
            dest.append(", ")
        }
        sendEmail(dest.toString(), subject, message)
    }

    @Throws(BusinessException::class)
    override fun sendNewPasswordEmail(destination: String, newPasswordCode: String) {
        sendEmail(destination, NEW_PASSWORD_SUBJECT, generateEmailNewPassword(newPasswordCode));
    }

    @Throws(BusinessException::class)
    override fun sendActivationEmail(destination: String, activationCode: String) {
        sendEmail(destination, USER_ACTIVATION_SUBJECT, generateEmailUserActivation(activationCode));
    }

    private fun generateEmailNewPassword(newPasswordCode: String): String {
        return ("<div style=\" text-align:center; width:450px; margin:10px auto; border-radius:10px;\">"
                + "<h1 style=\"color:#555555; font-size: 20px;\">Webstore</h1>"
                + "<p style=\"color:#555555; font-size: 16px; padding:10px; \">To generate a new password use the code below</p>"
                + "<p style=\"color:#555555; font-size: 16px;\"> " + newPasswordCode + " </p>" + "</div>")
    }

    private fun generateEmailUserActivation(activationCode: String): String {
        return ("<div style=\" text-align:center; width:450px; margin:10px auto; border-radius:10px;\">"
                + "<h1 style=\"color:#555555; font-size: 20px;\">Webstore</h1>"
                + "<p style=\"color:#555555; font-size: 16px; padding:10px; \">To activate your user use the code below</p>"
                + "<p style=\"color:#555555; font-size: 16px;\"> " + activationCode + " </p>" + "</div>")
    }

    private fun getSession(): Session {
        val mailPassword = "MAIL_PASSWORD"
        val emailFrom = environment.getProperty(MAIL_FROM)
        val password = environment.getProperty(mailPassword)
        return Session.getInstance(getProperties(), object : Authenticator() {
            override fun getPasswordAuthentication(): PasswordAuthentication {
                return PasswordAuthentication(emailFrom, password)
            }
        })
    }

    private fun getProperties(): Properties {
        val mailSmtpHost = "MAIL_SMTP_HOST"
        val mailSmtpPort = "MAIL_SMTP_PORT"
        val smtpHost = environment.getProperty(mailSmtpHost)
        val smtpPort = environment.getProperty(mailSmtpPort)
        val props = Properties()
        props["mail.smtp.host"] = smtpHost
        props["mail.smtp.socketFactory.port"] = smtpPort
        props["mail.smtp.socketFactory.class"] = "javax.net.ssl.SSLSocketFactory"
        props["mail.smtp.auth"] = "true"
        props["mail.smtp.port"] = smtpPort
        return props
    }

}