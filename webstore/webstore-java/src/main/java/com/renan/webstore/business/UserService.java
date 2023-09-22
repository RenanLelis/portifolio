package com.renan.webstore.business;

import com.renan.webstore.controller.form.user.ChangePasswordForm;

public interface UserService {
    
    void updateUserPassword(ChangePasswordForm form, Integer userID) throws BusinessException;
    
}
