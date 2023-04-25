package com.renan.todo.service;

import com.renan.todo.dto.UserDTO;
import com.renan.todo.dto.UserDTOMapper;
import com.renan.todo.model.User;
import com.renan.todo.persistence.UserRepository;
import com.renan.todo.util.MessageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Services for user authentication, reset password, register and activation
 */
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final MailService mailService;
    private final UserDTOMapper userDTOMapper;
    private final UtilService utilService;

    /**
     * Makes user authentication
     *
     * @param email    - user email
     * @param password - password
     *
     * @return - the user logged-in
     * @throws BusinessException - in cases of any errors or invalid operations
     */
    public UserDTO login(String email, String password) throws BusinessException {
        if (!utilService.isMail(email) || password.length() < User.MIN_LENGTH_PASSWORD) {
            throw new BusinessException(MessageUtil.getErrorMessageInputValues(),
                    BusinessException.BUSINESS_MESSAGE,
                    AppErrorType.INVALID_INPUT);
        }
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty() ||
                comparePasswordWithHash(userOptional.get().getPassword(), password)) {
            throw new BusinessException(MessageUtil.getErrorMessageLogin(),
                    BusinessException.BUSINESS_MESSAGE,
                    AppErrorType.INVALID_INPUT);
        }
        if (userOptional.get().getUserStatus().equals(User.STATUS_INACTIVE)) {
            throw new BusinessException(MessageUtil.getErrorMessageUserNotActive(),
                    BusinessException.BUSINESS_MESSAGE,
                    AppErrorType.INVALID_INPUT);
        }
        return userDTOMapper.apply(userOptional.get());
    }

    /**
     * Generate a code for password reset
     *
     * @param email - user email
     *
     * @throws BusinessException - in cases of any errors or invalid operations
     */
    public void recoverPassword(String email) throws BusinessException {
        if (!utilService.isMail(email)) {
            throw new BusinessException(MessageUtil.getErrorMessageInputValues(),
                    BusinessException.BUSINESS_MESSAGE,
                    AppErrorType.INVALID_INPUT);
        }
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            throw new BusinessException(MessageUtil.getErrorMessageUserNotFound(),
                    BusinessException.BUSINESS_MESSAGE,
                    AppErrorType.INVALID_INPUT);
        }
        User user = userOptional.get();
        if (user.getUserStatus().equals(User.STATUS_INACTIVE)) {
            throw new BusinessException(MessageUtil.getErrorMessageUserNotActive(),
                    BusinessException.BUSINESS_MESSAGE,
                    AppErrorType.INVALID_INPUT);
        }
        String newPasswordCode = utilService.generateRandomString();
        user.setNewPasswordCode(newPasswordCode);
        mailService.sendNewPasswordEmail(user.getEmail(), newPasswordCode);
        userRepository.save(user);
    }

    /**
     * Reset the password and make user authentication
     *
     * @param email           - user email
     * @param newPassword     - the new user password
     * @param newPasswordCode - the code to validate and change the password
     *
     * @return - the user logged-in
     * @throws BusinessException - in cases of any errors or invalid operations
     */
    public UserDTO resetPasswordByCode(String email, String newPassword, String newPasswordCode) throws BusinessException {
        if (!validateResetPasswordInput(email, newPassword, newPasswordCode)) {
            throw new BusinessException(MessageUtil.getErrorMessageInputValues(),
                    BusinessException.BUSINESS_MESSAGE,
                    AppErrorType.INVALID_INPUT);
        }
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            throw new BusinessException(MessageUtil.getErrorMessageUserNotFound(),
                    BusinessException.BUSINESS_MESSAGE,
                    AppErrorType.INVALID_INPUT);
        }
        User user = userOptional.get();
        if (!user.getNewPasswordCode().equals(newPasswordCode.trim())) {
            throw new BusinessException(MessageUtil.getErrorMessageInputValues(),
                    BusinessException.BUSINESS_MESSAGE,
                    AppErrorType.INVALID_INPUT);
        }
        user.setNewPasswordCode(null);
        user.setUserStatus(User.STATUS_ACTIVE);
        user.setActivationCode(null);
        user.setPassword(utilService.generateHashString(newPassword.trim()));
        userRepository.save(user);
        return userDTOMapper.apply(user);
    }

    /**
     * Create new user on the database
     *
     * @param email    - user email
     * @param password - user password
     * @param name     - user first name
     * @param lastName - user last name
     *
     * @throws BusinessException - in cases of any errors or invalid operations
     */
    public void registerUser(String email, String password, String name, String lastName) throws BusinessException {
        //TODO
    }

    /**
     * Activate the user on the system
     *
     * @param email          - user email
     * @param activationCode - the code to validate and activate the user
     *
     * @throws BusinessException - in cases of any errors or invalid operations
     */
    public UserDTO activateUser(String email, String activationCode) throws BusinessException {
        //TODO
        return null;
    }

    /**
     * Generate activation code and send by email
     *
     * @param email - user email
     *
     * @throws BusinessException - in cases of any errors or invalid operations
     */
    public void requestUserActivation(String email) throws BusinessException {
        //TODO
    }

    /**
     * Compare a string password with a hash to validate login operation
     *
     * @param hash     - hash password stored on database
     * @param password - password passed as parameter for login operation
     *
     * @return - true if are the same password, false if are not
     */
    private boolean comparePasswordWithHash(String hash, String password) {
        return hash.equals(utilService.generateHashString(password));
    }

    /**
     * Validate the input for reset password operation
     *
     * @param email           - user email
     * @param newPassword     - new password
     * @param newPasswordCode - code to validate
     *
     * @return - true if is valida
     */
    private boolean validateResetPasswordInput(String email, String newPassword, String newPasswordCode) {
        return utilService.isMail(email)
                && newPasswordCode != null
                && newPassword != null
                && newPasswordCode.trim().length() != User.LENGTH_NEW_PASSWORD_CODE
                && newPassword.trim().length() >= User.MIN_LENGTH_PASSWORD;
    }

}