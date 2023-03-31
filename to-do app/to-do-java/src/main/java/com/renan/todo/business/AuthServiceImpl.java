package com.renan.todo.business;

import com.renan.todo.dto.UserDTO;
import com.renan.todo.dto.UserDTOMapper;
import com.renan.todo.model.User;
import com.renan.todo.persistence.UserRepository;
import com.renan.todo.util.CriptoUtil;
import com.renan.todo.util.MessageUtil;
import com.renan.todo.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Services for user authentication, reset password, register and activation
 */
@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepository userRepository;
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
        if (!StringUtil.isMail(email) || password.length() < 6) {
            throw new BusinessException(
                    MessageUtil.getErrorMessageInputValues(),
                    BusinessException.BUSINESS_MESSAGE,
                    AppErrorType.INVALID_INPUT);
        }
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty() ||
                comparePasswordWithHash(userOptional.get().getPassword(), password)) {
            throw new BusinessException(
                    MessageUtil.getErrorMessageLogin(),
                    BusinessException.BUSINESS_MESSAGE,
                    AppErrorType.INVALID_INPUT);
        }
        if (userOptional.get().getUserStatus().equals(User.STATUS_INACTIVE)) {
            throw new BusinessException(
                    MessageUtil.getErrorMessageUserNotActive(),
                    BusinessException.BUSINESS_MESSAGE,
                    AppErrorType.INVALID_INPUT);
        }
        return userDTOMapper.apply(userOptional.get());
    }

    /**
     * Compare a string password with a hash to validate login operation
     *
     * @param hash     - hash password stored on database
     * @param password - password passed as parameter for login operation
     * @return - true if are the same password, false if are not
     */
    private boolean comparePasswordWithHash(String hash, String password) {
        return hash.equals(CriptoUtil.generateHashString(password));
    }

    /**
     * Generate a code for password reset
     *
     * @param email - user email
     * @throws BusinessException - in cases of any errors or invalid operations
     */
    public void recoverPassword(String email) throws BusinessException {
        if (StringUtil.isEmpty(email) || !StringUtil.isMail(email)) {
            throw new BusinessException(
                    MessageUtil.getErrorMessageInputValues(),
                    BusinessException.BUSINESS_MESSAGE,
                    AppErrorType.INVALID_INPUT);
        }
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty() || optionalUser.get().getId() <= 0) {
            throw new BusinessException(
                    MessageUtil.getErrorMessageUserNotFound(),
                    BusinessException.BUSINESS_MESSAGE,
                    AppErrorType.INVALID_INPUT);
        }
        String newPasswordCode = generateNewPasswordCode();
        userRepository.setNewPasswordCode(email, newPasswordCode);
        mailService.sendNewPasswordEmail(email, newPasswordCode);
    }

    /**
     * Generates a new password code for the user
     *
     * @return - the code
     */
    private String generateNewPasswordCode() {
        return StringUtil.generateRandomString(User.LENGTH_NEW_PASSWORD_CODE);
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
        if (StringUtil.isEmpty(email) || !StringUtil.isMail(email) || StringUtil.isEmpty(newPassword) || StringUtil.isEmpty(newPasswordCode)) {
            throw new BusinessException(
                    MessageUtil.getErrorMessageInputValues(),
                    BusinessException.BUSINESS_MESSAGE,
                    AppErrorType.INVALID_INPUT);
        }
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty() || userOptional.get().getNewPasswordCode() == null || !userOptional.get().getNewPasswordCode().equals(newPasswordCode)) {
            throw new BusinessException(
                    MessageUtil.getErrorMessageUserNotFound(),
                    BusinessException.BUSINESS_MESSAGE,
                    AppErrorType.INVALID_INPUT);
        }
        String hashPassword = CriptoUtil.generateHashString(newPassword);
        userRepository.resetUserPasswordByCode(email, hashPassword, newPasswordCode);
        return userDTOMapper.apply(userOptional.get());
    }

    /**
     * Create new user on the database
     *
     * @param email    - user email
     * @param password - user password
     * @param name     - user first name
     * @param lastName - user last name
     * @throws BusinessException - in cases of any errors or invalid operations
     */
    public void registerUser(String email, String password, String name, String lastName) throws BusinessException {
        if (!validateNewUserData(email, password, name)) {
            throw new BusinessException(
                    MessageUtil.getErrorMessageInputValues(),
                    BusinessException.BUSINESS_MESSAGE,
                    AppErrorType.INVALID_INPUT);
        }
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent() && userOptional.get().getId() != null && userOptional.get().getId() > 0) {
            throw new BusinessException(
                    MessageUtil.getErrorMessageEmailAlreadyExists(),
                    BusinessException.BUSINESS_MESSAGE,
                    AppErrorType.INVALID_INPUT);
        }
        String hashPassword = CriptoUtil.generateHashString(password);
        User newUser = userRepository.save(new User(
                null,
                name,
                lastName,
                email,
                hashPassword,
                generateActivationCode(),
                null,
                User.STATUS_INACTIVE));
        mailService.sendActivationEmail(newUser.getEmail(), newUser.getActivationCode());
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
        return
                StringUtil.isNotEmpty(password) &&
                        StringUtil.isNotEmpty(name) &&
                        StringUtil.isNotEmpty(email) &&
                        StringUtil.isMail(email);
    }

    /**
     * Generates a activation code for the user
     *
     * @return - the code
     */
    private String generateActivationCode() {
        return StringUtil.generateRandomString(User.LENGTH_NEW_PASSWORD_CODE);
    }

    /**
     * Activate the user on the system
     *
     * @param email          - user email
     * @param activationCode - the code to validate and activate the user
     * @return - the user logged-in
     * @throws BusinessException - in cases of any errors or invalid operations
     */
    public UserDTO activateUser(String email, String activationCode) throws BusinessException {
        if (!StringUtil.isMail(email) || activationCode.length() != 6) {
            throw new BusinessException(
                    MessageUtil.getErrorMessageInputValues(),
                    BusinessException.BUSINESS_MESSAGE,
                    AppErrorType.INVALID_INPUT);
        }
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty() || userOptional.get().getId() == null || userOptional.get().getId() <= 0) {
            throw new BusinessException(
                    MessageUtil.getErrorMessageEmailAlreadyExists(),
                    BusinessException.BUSINESS_MESSAGE,
                    AppErrorType.INVALID_INPUT);
        }
        userRepository.activateUser(email, activationCode, User.STATUS_ACTIVE);
        User user = userOptional.get();
        user.setActivationCode(null);
        user.setUserStatus(User.STATUS_ACTIVE);
        return userDTOMapper.apply(user);
    }

    /**
     * Generate activation code and send by email
     *
     * @param email - user email
     * @throws BusinessException - in cases of any errors or invalid operations
     */
    public void requestUserActivation(String email) throws BusinessException {
        if (!StringUtil.isMail(email)) {
            throw new BusinessException(
                    MessageUtil.getErrorMessageInputValues(),
                    BusinessException.BUSINESS_MESSAGE,
                    AppErrorType.INVALID_INPUT);
        }
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            throw new BusinessException(
                    MessageUtil.getErrorMessageUserNotFound(),
                    BusinessException.BUSINESS_MESSAGE,
                    AppErrorType.INVALID_INPUT);
        }
        User user = userOptional.get();
        user.setActivationCode(generateActivationCode());
        user = userRepository.save(user);
        mailService.sendActivationEmail(user.getEmail(), user.getActivationCode());
    }

}
