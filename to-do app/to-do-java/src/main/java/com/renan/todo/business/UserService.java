package com.renan.todo.business;

/**
 * Services for user info update and password update
 */
public interface UserService {
    
    /**
     * Update information about the user
     *
     * @param id       - user id
     * @param name     - user first name
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
