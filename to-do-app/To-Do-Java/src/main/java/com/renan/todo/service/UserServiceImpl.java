package com.renan.todo.service;

import com.renan.todo.model.User;
import com.renan.todo.persistence.UserRepository;
import com.renan.todo.util.MessageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Services for user info update and password update
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UtilService utilService;

    /**
     * Update information about the user
     *
     * @param id       - user id
     * @param name     - user first name
     * @param lastName - last name
     *
     * @throws BusinessException - in cases of any errors or invalid operations
     */
    public void updateUser(Integer id, String name, String lastName) throws BusinessException {
        if (!validateUpdateUserInput(id, name)) {
            throw new BusinessException(MessageUtil.getErrorMessageInputValues(),
                    BusinessException.BUSINESS_MESSAGE,
                    AppErrorType.INVALID_INPUT);
        }
        userRepository.updateUserData(id, name.trim(), lastName != null ? lastName.trim() : null);
    }

    /**
     * Update information about the user
     *
     * @param id       - user id
     * @param password - new password
     *
     * @throws BusinessException - in cases of any errors or invalid operations
     */
    public void updateUserPassword(Integer id, String password) throws BusinessException {
        if (!validateUserUpdatePasswordInput(id, password)) {
            throw new BusinessException(MessageUtil.getErrorMessageInputValues(),
                    BusinessException.BUSINESS_MESSAGE,
                    AppErrorType.INVALID_INPUT);
        }
        userRepository.updateUserPassword(id, utilService.generateHashString(password.trim()));
    }

    /**
     * Validate the input data for user update operation
     *
     * @param id   - user id
     * @param name - user first name
     *
     * @return - true if input data is valid
     */
    private boolean validateUpdateUserInput(Integer id, String name) {
        return id != null && id > 0 && name != null && name.trim().length() > 0;
    }

    /**
     * Validate the input data for user password update operation
     *
     * @param id       - user id
     * @param password - new password
     *
     * @return - true if is valid
     */
    private boolean validateUserUpdatePasswordInput(Integer id, String password) {
        return id != null && id > 0 && password != null && password.trim().length() > User.MIN_LENGTH_PASSWORD;
    }
}