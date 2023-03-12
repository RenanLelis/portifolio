package com.renan.todo.business;

import com.renan.todo.dto.UserDTO;
import com.renan.todo.dto.UserDTOMapper;
import com.renan.todo.entities.User;
import com.renan.todo.persistence.UserRepository;
import com.renan.todo.util.CriptoUtil;
import com.renan.todo.util.MessageUtil;
import com.renan.todo.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Services for user authentication, reset password, register and change his data
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository repository;
    @Autowired
    private MailService mailService;

    @Autowired
    private UserDTOMapper userDTOMapper;

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
        String hashPassword = CriptoUtil.generateHashString(password);
        if (user == null || !user.getPassword().equals(hashPassword)) {
            throw new BusinessException(MessageUtil.getErrorMessageLogin(), BusinessException.BUSINESS_MESSAGE, AppErrorType.INVALID_INPUT);
        }
        return userDTOMapper.apply(user);
    }

    /**
     * Generate a code for password reset
     *
     * @param email - user email
     * @throws BusinessException - in cases of any errors or invalid operations
     */
    public void recoverPassword(String email) throws BusinessException {
        if (StringUtil.isEmpty(email) || !StringUtil.ismail(email)) {
            throw new BusinessException(MessageUtil.getErrorMessageInputValues(), BusinessException.BUSINESS_MESSAGE, AppErrorType.INVALID_INPUT);
        }
        User user = repository.findByEmail(email);
        if (user == null || user.getId() <= 0) {
            throw new BusinessException(MessageUtil.getErrorMessageInputValues(), BusinessException.BUSINESS_MESSAGE, AppErrorType.INVALID_INPUT);
        }
        String newPasswordCode = StringUtil.generateRandomString(User.LENGTH_NEW_PASSWORD_CODE);
        user.setNewPasswordCode(newPasswordCode);
        repository.setNewPasswordCode(email, newPasswordCode);
        mailService.sendNewPasswordEmail(email, newPasswordCode);
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
        if (StringUtil.isEmpty(email) || !StringUtil.ismail(email) || StringUtil.isEmpty(newPasswordCode)) {
            throw new BusinessException(MessageUtil.getErrorMessageInputValues(), BusinessException.BUSINESS_MESSAGE, AppErrorType.INVALID_INPUT);
        }
        User user = repository.findByEmail(email);
        if (user == null || user.getActivationCode() == null || !user.getNewPasswordCode().equals(newPasswordCode)) {
            throw new BusinessException(MessageUtil.getErrorMessageInputValues(), BusinessException.BUSINESS_MESSAGE, AppErrorType.INVALID_INPUT);
        }

        String hashPassword = CriptoUtil.generateHashString(newPassword);
        repository.resetUserPasswordByCode(email, hashPassword, newPasswordCode);
        user.setNewPasswordCode(null);

        return userDTOMapper.apply(user);
    }

    /**
     * Create new user on the database
     *
     * @param email    - user email
     * @param password - user password
     * @param name     - user first name
     * @param lastName - user last name
     * @return - the user created and logged-in but with status inactive
     * @throws BusinessException - in cases of any errors or invalid operations
     */
    public UserDTO registerUser(String email, String password, String name, String lastName) throws BusinessException {
        if (!validateNewUserData(email, password, name)) {
            throw new BusinessException(MessageUtil.getErrorMessageInputValues(), BusinessException.BUSINESS_MESSAGE, AppErrorType.INVALID_INPUT);
        }
        User user = repository.findByEmail(email);
        if (user != null && user.getId() != null && user.getId() > 0) {
            throw new BusinessException(MessageUtil.getErrorMessageEmailAlreadyExists(), BusinessException.BUSINESS_MESSAGE, AppErrorType.INVALID_INPUT);
        }
        String hashPassword = CriptoUtil.generateHashString(password);
        String activationCode = StringUtil.generateRandomString(User.LENGTH_ACTIVATION_CODE);
        user = new User(null, name, lastName, email, hashPassword, activationCode, null, User.STATUS_INACTIVE);
        User newUser = repository.save(user);
        mailService.sendActivationEmail(newUser.getEmail(), newUser.getActivationCode());
        return userDTOMapper.apply(user);
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
        if (StringUtil.isEmpty(email) || !StringUtil.ismail(email) || StringUtil.isEmpty(activationCode)) {
            throw new BusinessException(MessageUtil.getErrorMessageInputValues(), BusinessException.BUSINESS_MESSAGE, AppErrorType.INVALID_INPUT);
        }
        User user = repository.findByEmail(email);
        if (user.getActivationCode() == null || !user.getActivationCode().equals(activationCode)) {
            throw new BusinessException(MessageUtil.getErrorMessageInputValues(), BusinessException.BUSINESS_MESSAGE, AppErrorType.INVALID_INPUT);
        }

        repository.activateUser(email, activationCode, User.STATUS_ACTIVE);
        user.setUserStatus(User.STATUS_ACTIVE);
        user.setActivationCode(null);

        return userDTOMapper.apply(user);
    }

    /**
     * Update information about the user
     *
     * @param id       - user id
     * @param name     - user first name
     * @param lastName - last name
     * @throws BusinessException - in cases of any errors or invalid operations
     */
    public void updateUser(Integer id, String name, String lastName) throws BusinessException {
        if (id == null || id <= 0 || StringUtil.isEmpty(name)) {
            throw new BusinessException(MessageUtil.getErrorMessageInputValues(), BusinessException.BUSINESS_MESSAGE, AppErrorType.INVALID_INPUT);
        }
        repository.updateUserInfo(id, name, lastName);
    }

    /**
     * Update information about the user
     *
     * @param id       - user id
     * @param password - new password
     * @throws BusinessException - in cases of any errors or invalid operations
     */
    public void updateUserPassword(Integer id, String password) throws BusinessException {
        if (id == null || id <= 0 || StringUtil.isEmpty(password)) {
            throw new BusinessException(MessageUtil.getErrorMessageInputValues(), BusinessException.BUSINESS_MESSAGE, AppErrorType.INVALID_INPUT);
        }
        String hashPassword = CriptoUtil.generateHashString(password);
        repository.updateUserPassword(id, hashPassword);
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

    /**
     * Validate the input for new user registration
     *
     * @param email    - user email
     * @param password - user password
     * @param name     - user first name
     * @return - true if is valid
     */
    private boolean validateNewUserData(String email, String password, String name) {
        return StringUtil.isNotEmpty(password) && StringUtil.isNotEmpty(name) && StringUtil.isNotEmpty(email) && StringUtil.ismail(email);
    }

}
