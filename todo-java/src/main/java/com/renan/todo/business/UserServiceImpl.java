package com.renan.todo.business;

import com.renan.todo.dto.UserDTO;
import com.renan.todo.entities.User;
import com.renan.todo.persistence.UserRepository;
import com.renan.todo.util.CriptoUtil;
import com.renan.todo.util.MessageUtil;
import com.renan.todo.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Services for user authentication, reset password, register and change his data
 */
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository repository;

    /**
     * Makes user authentication
     *
     * @param email    - user email
     * @param password - password
     * @return - the user logged-in
     * @throws BusinessException - in cases of any errors or invalid operations
     */
    public UserDTO login(String email, String password) throws BusinessException {
        if (!validateLoginData(email, password)) {
            throw new BusinessException(MessageUtil.getErrorMessageInputValues(), BusinessException.BUSINESS_MESSAGE, AppErrorType.INVALID_INPUT);
        }
        User user = repository.findByEmail(email);
        if (user == null) {
            throw new BusinessException(MessageUtil.getErrorMessageLogin(), BusinessException.BUSINESS_MESSAGE, AppErrorType.INVALID_INPUT);
        }
        String hashPassword = CriptoUtil.generateHashString(password);
        if () {}
        return null;
    }

    /**
     * Generate a code for password reset
     *
     * @param email - user email
     * @throws BusinessException - in cases of any errors or invalid operations
     */
    public void recoverPassword(String email) throws BusinessException {
        //TODO Implement
    }

    /**
     * Reset the password and make user authentication
     *
     * @param email           - user email
     * @param newPassword     - the new user password
     * @param newPasswordCode - the code to validate and change the password
     * @return - the user logged-in
     * @throws BusinessException - in cases of any errors or invalid operations
     */
    public UserDTO resetPasswordByCode(String email, String newPassword, String newPasswordCode) throws BusinessException {
        //TODO Implement
        return null;
    }

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
    public UserDTO registerUser(String email, String password, String name, String lastName) throws BusinessException {
        //TODO Implement
        return null;
    }

    /**
     * Reset the password and make user authentication
     *
     * @param email          - user email
     * @param activationCode - the code to validate and activate the user
     * @return - the user logged-in
     * @throws BusinessException - in cases of any errors or invalid operations
     */
    public UserDTO activateUser(String email, String activationCode) throws BusinessException {
        //TODO Implement
        return null;
    }

    /**
     * Validate the user data for login operation
     *
     * @param email    - user email
     * @param password - user password
     * @return - true if is valid
     */
    private boolean validateLoginData(String email, String password) {
        return StringUtil.isNotEmpty(password) && StringUtil.isNotEmpty(email) && StringUtil.ismail(email);
    }

}
