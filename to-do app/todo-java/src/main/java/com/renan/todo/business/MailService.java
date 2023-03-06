package com.renan.todo.business;

import java.util.List;

/**
 * Service for sending e-mail for the users
 */
public interface MailService {

    /**
     * Send email to the user (destination)
     *
     * @param destination - user email
     * @param subject     - email subject
     * @param message     - message to be sent
     * @throws BusinessException - In case of any fail
     */
    public void sendEmail(String destination, String subject, String message) throws BusinessException;

    /**
     * Send email to the users (destinationList)
     *
     * @param destinationList - list of users emails
     * @param subject         - email subject
     * @param message         - message to be sent
     * @throws BusinessException - In case of any fail
     */
    public void sendEmail(List<String> destinationList, String subject, String message) throws BusinessException;

    /**
     * Send email to the user to reset the password
     *
     * @param destination     - user email
     * @param newPasswordCode - the code to change the password
     * @throws BusinessException - In case of any fail
     */
    public void sendNewPasswordEmail(String destination, String newPasswordCode) throws BusinessException;

    /**
     * Send email to the user to activate him on the systema
     *
     * @param destination    - user email
     * @param activationCode - the code to activate the user
     * @throws BusinessException - In case of any fail
     */
    public void sendActivationEmail(String destination, String activationCode) throws BusinessException;
}
