package com.renan.todo.business;

import com.renan.todo.util.MessageUtil;
import jakarta.mail.*;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Properties;

/**
 * Service for sending e-mail for the users
 */
@Service
public class MailServiceImpl implements MailService {

    public static final String NEW_PASSWORD_SUBJECT = "Change your password - TODO-APP";
    public static final String USER_ACTIVATION_SUBJECT = "Activate your user - TODO-APP";

    private final String emailFrom = "";
    private final String password = "";
    private final String smtpHost = "";
    private final String smtpPort = "";

//    private String emailFrom = System.getenv("MAIL_FROM");
//    private String password = System.getenv("MAIL_PASSWORD");
//    private String smtpHost = System.getenv("MAIL_SMTP_HOST");
//    private String smtpPort = System.getenv("MAIL_SMTP_PORT");

    /**
     * Send email to the user (destination)
     *
     * @param destination - user email
     * @param subject     - email subject
     * @param message     - message to be sent
     *
     * @throws BusinessException - In case of any fail
     */
    public void sendEmail(String destination, String subject, String message) throws BusinessException {
        Session session = getSession();
        MimeMessage mimeMessage = new MimeMessage(session);
        try {
            mimeMessage.setFrom(new InternetAddress(emailFrom));
            Address[] toUser = InternetAddress.parse(destination);
            mimeMessage.setRecipients(Message.RecipientType.TO, toUser);
            mimeMessage.setSubject(subject);
            mimeMessage.setText(message, "utf-8", "html");
            Transport.send(mimeMessage);
        } catch (AddressException e) {
            throw new BusinessException(MessageUtil.getErrorMessage(), true, AppErrorType.INTERN_ERROR);
        } catch (MessagingException e) {
            throw new BusinessException(MessageUtil.getErrorMessage(), true, AppErrorType.INTERN_ERROR);
        }
    }

    /**
     * Send email to the users (destinationList)
     *
     * @param destinationList - list of users emails
     * @param subject         - email subject
     * @param message         - message to be sent
     *
     * @throws BusinessException - In case of any fail
     */
    public void sendEmail(List<String> destinationList, String subject, String message) throws BusinessException {
        StringBuilder dest = new StringBuilder();
        for (String destination : destinationList) {
            dest.append(destination);
            dest.append(", ");
        }
        sendEmail(dest.toString(), subject, message);
    }

    /**
     * Send email to the user to reset the password
     *
     * @param destination     - user email
     * @param newPasswordCode - the code to change the password
     *
     * @throws BusinessException - In case of any fail
     */
    public void sendNewPasswordEmail(String destination, String newPasswordCode) throws BusinessException {
        sendEmail(destination, NEW_PASSWORD_SUBJECT, generateEmailNewPassword(newPasswordCode));
    }

    /**
     * Send email to the user to activate him on the systema
     *
     * @param destination    - user email
     * @param activationCode - the code to activate the user
     *
     * @throws BusinessException - In case of any fail
     */
    public void sendActivationEmail(String destination, String activationCode) throws BusinessException {
        sendEmail(destination, USER_ACTIVATION_SUBJECT, generateEmailUserActivation(activationCode));
    }

    /**
     * generate the email message with the new code to change password
     *
     * @param newPasswordCode - code to change the password
     *
     * @return - the message for the email
     */
    private String generateEmailNewPassword(String newPasswordCode) {
        return "<div style=\" text-align:center; width:450px; margin:10px auto; border-radius:10px;\">"
                + "<h1 style=\"color:#b67509; font-size: 20px;\">TODO-APP</h1>"
                + "<p style=\"color:#b67509; font-size: 16px; padding:10px; \">To generate a new password use the code below</p>"
                + "<p style=\"color:#b67509; font-size: 16px;\"> " + newPasswordCode + " </p>" + "</div>";
    }

    /**
     * generate the email message with the new code to activate the user
     *
     * @param activationCode - code to activate the user
     *
     * @return - the message for the email
     */
    private String generateEmailUserActivation(String activationCode) {
        return "<div style=\" text-align:center; width:450px; margin:10px auto; border-radius:10px;\">"
                + "<h1 style=\"color:#b67509; font-size: 20px;\">TODO-APP</h1>"
                + "<p style=\"color:#b67509; font-size: 16px; padding:10px; \">To activate your user use the code below</p>"
                + "<p style=\"color:#b67509; font-size: 16px;\"> " + activationCode + " </p>" + "</div>";
    }

    /**
     * Cria sessao de conexao ao email
     *
     * @return
     */
    private Session getSession() {
        return Session.getInstance(getProperties(), new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(emailFrom, password);
            }
        });
    }

    /**
     * Load properties for connection with the smtp server
     *
     * @return - properties loaded
     */
    private Properties getProperties() {
        Properties props = new Properties();
        props.put("mail.smtp.host", smtpHost);
        props.put("mail.smtp.socketFactory.port", smtpPort);
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", smtpPort);
        return props;
    }
}