package com.renan.todo.business;

import com.renan.todo.persistence.UserRepository;
import com.renan.todo.util.CriptoUtil;
import com.renan.todo.util.MessageUtil;
import com.renan.todo.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Services for user info update and password update
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository repository;

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
            throw new BusinessException(
                    MessageUtil.getErrorMessageInputValues(),
                    BusinessException.BUSINESS_MESSAGE,
                    AppErrorType.INVALID_INPUT);
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
            throw new BusinessException(
                    MessageUtil.getErrorMessageInputValues(),
                    BusinessException.BUSINESS_MESSAGE,
                    AppErrorType.INVALID_INPUT);
        }
        String hashPassword = CriptoUtil.generateHashString(password);
        repository.updateUserPassword(id, hashPassword);
    }

}
