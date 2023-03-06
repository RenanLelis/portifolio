package com.renan.todo.business;

import com.renan.todo.dto.UserDTO;

/**
 * Services for user authentication, reset password, register and change his data
 */
public interface UserService {

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
     * @param name     - user name
     * @param lastName - user last name
     * @return - the user created and logged-in but with status inactive
     * @throws BusinessException - in cases of any errors or invalid operations
     */
    public UserDTO registerUser(String email, String password, String name, String lastName) throws BusinessException;

    /**
     * Reset the password and make user authentication
     *
     * @param email          - user email
     * @param activationCode - the code to validate and activate the user
     * @return - the user logged-in
     * @throws BusinessException - in cases of any errors or invalid operations
     */
    public UserDTO activateUser(String email, String activationCode) throws BusinessException;

    /**
     * Update information about the user
     *
     * @param id       - user id
     * @param name     - name of user
     * @param lastName - last name
     * @throws BusinessException - in cases of any errors or invalid operations
     */
    public void updateUser(Integer id, String name, String lastName) throws BusinessException;

    /**
     * Update information about the user
     *
     * @param id       - user id
     * @param password - new password
     * @throws BusinessException - in cases of any errors or invalid operations
     */
    public void updateUserPassword(Integer id, String password) throws BusinessException;
}
