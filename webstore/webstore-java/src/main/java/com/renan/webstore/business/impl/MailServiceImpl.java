package com.renan.webstore.business.impl;

import com.renan.webstore.business.AppErrorType;
import com.renan.webstore.business.BusinessException;
import com.renan.webstore.business.MailService;
import com.renan.webstore.util.MessageUtil;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Properties;

@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {
    
    public static final String NEW_PASSWORD_SUBJECT = "Change your password - Webstore";
    public static final String USER_ACTIVATION_SUBJECT = "Activate your user - Webstore";
    
    private final Environment environment;
    
    private final String MAIL_FROM = "MAIL_FROM";
    
    public void sendEmail(String destination, String subject, String message) throws BusinessException {
        String emailFrom = environment.getProperty(MAIL_FROM);
        Session session = getSession();
        MimeMessage mimeMessage = new MimeMessage(session);
        try {
            assert emailFrom != null;
            mimeMessage.setFrom(new InternetAddress(emailFrom));
            Address[] toUser = InternetAddress.parse(destination);
            mimeMessage.setRecipients(Message.RecipientType.TO, toUser);
            mimeMessage.setSubject(subject);
            mimeMessage.setText(message, "utf-8", "html");
            Transport.send(mimeMessage);
        } catch (MessagingException e) {
            throw new BusinessException(MessageUtil.getErrorMessage(), true, AppErrorType.INTERN_ERROR);
        }
    }
    
    public void sendEmail(List<String> destinationList, String subject, String message) throws BusinessException {
        StringBuilder dest = new StringBuilder();
        for (String destination : destinationList) {
            dest.append(destination);
            dest.append(", ");
        }
        sendEmail(dest.toString(), subject, message);
    }
    
    public void sendNewPasswordEmail(String destination, String newPasswordCode) throws BusinessException {
        sendEmail(destination, NEW_PASSWORD_SUBJECT, generateEmailNewPassword(newPasswordCode));
    }
    
    public void sendActivationEmail(String destination, String activationCode) throws BusinessException {
        sendEmail(destination, USER_ACTIVATION_SUBJECT, generateEmailUserActivation(activationCode));
    }
    
    private String generateEmailNewPassword(String newPasswordCode) {
        return "<div style=\" text-align:center; width:450px; margin:10px auto; border-radius:10px;\">"
                + "<h1 style=\"color:#555555; font-size: 20px;\">Webstore</h1>"
                + "<p style=\"color:#555555; font-size: 16px; padding:10px; \">To generate a new password use the code below</p>"
                + "<p style=\"color:#555555; font-size: 16px;\"> " + newPasswordCode + " </p>" + "</div>";
    }
    
    private String generateEmailUserActivation(String activationCode) {
        return "<div style=\" text-align:center; width:450px; margin:10px auto; border-radius:10px;\">"
                + "<h1 style=\"color:#555555; font-size: 20px;\">Webstore</h1>"
                + "<p style=\"color:#555555; font-size: 16px; padding:10px; \">To activate your user use the code below</p>"
                + "<p style=\"color:#555555; font-size: 16px;\"> " + activationCode + " </p>" + "</div>";
    }
    
    private Session getSession() {
        String emailFrom = environment.getProperty(MAIL_FROM);
        String MAIL_PASSWORD = "MAIL_PASSWORD";
        String password = environment.getProperty(MAIL_PASSWORD);
        return Session.getInstance(getProperties(), new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(emailFrom, password);
            }
        });
    }
    
    private Properties getProperties() {
        String MAIL_SMTP_HOST = "MAIL_SMTP_HOST";
        String MAIL_SMTP_PORT = "MAIL_SMTP_PORT";
        String smtpHost = environment.getProperty(MAIL_SMTP_HOST);
        String smtpPort = environment.getProperty(MAIL_SMTP_PORT);
        Properties props = new Properties();
        props.put("mail.smtp.host", smtpHost);
        props.put("mail.smtp.socketFactory.port", smtpPort);
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", smtpPort);
        return props;
    }
    
}

