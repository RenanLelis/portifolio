package com.renan.todo.service;

import com.renan.todo.dto.UserDTO;

/**
 * Services for user authentication, reset password, register and activation
 */
public interface AuthService {

    /**
     * Makes user authentication
     *
     * @param email    - user email
     * @param password - password
     * @return - the user logged-in
     * @throws BusinessException - in cases of any errors or invalid operations
     */
    public UserDTO login(String email, String password) throws BusinessException;

    /**
     * Generate a code for password reset
     *
     * @param email - user email
     * @throws BusinessException - in cases of any errors or invalid operations
     */
    public void recoverPassword(String email) throws BusinessException;

    /**
     * Reset the password and make user authentication
     *
     * @param email           - user email
     * @param newPassword     - the new user password
     * @param newPasswordCode - the code to validate and change the password
     * @return - the user logged-in
     * @throws BusinessException - in cases of any errors or invalid operations
     */
    public UserDTO resetPasswordByCode(String email, String newPassword, String newPasswordCode) throws BusinessException;

    /**
     * Create new user on the database
     *
     * @param email    - user email
     * @param password - user password
     * @param name     - user first name
     * @param lastName - user last name
     * @throws BusinessException - in cases of any errors or invalid operations
     */
    public void registerUser(String email, String password, String name, String lastName) throws BusinessException;

    /**
     * Activate the user on the system
     *
     * @param email          - user email
     * @param activationCode - the code to validate and activate the user
     * @throws BusinessException - in cases of any errors or invalid operations
     */
    public UserDTO activateUser(String email, String activationCode) throws BusinessException;

    /**
     * Generate activation code and send by email
     *
     * @param email - user email
     * @throws BusinessException - in cases of any errors or invalid operations
     */
    public void requestUserActivation(String email) throws BusinessException;

}