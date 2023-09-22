package com.renan.webstore.business.impl;

import com.renan.webstore.business.AppErrorType;
import com.renan.webstore.business.BusinessException;
import com.renan.webstore.business.UserService;
import com.renan.webstore.business.ValidatorService;
import com.renan.webstore.controller.form.user.ChangePasswordForm;
import com.renan.webstore.model.AppUser;
import com.renan.webstore.persistence.repository.UserRepository;
import com.renan.webstore.util.MessageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final ValidatorService validatorService;
    @Override
    public void updateUserPassword(ChangePasswordForm form, Integer userID) throws BusinessException {
        validateChangePasswordInput(form, userID);
        Optional<AppUser> userOpt = repository.findById(userID);
        if (userOpt.isEmpty()) throw new BusinessException(
                MessageUtil.getErrorMessageUserNotFound(),
                BusinessException.BUSINESS_MESSAGE,
                AppErrorType.INVALID_INPUT
        );
        AppUser user = userOpt.get();
        user.setPassword(passwordEncoder.encode(form.getNewPassword().trim()));
        repository.save(user);
    }
    
    private void validateChangePasswordInput(ChangePasswordForm form, Integer userID) throws BusinessException {
        if (userID == null || userID <= 0 || form == null || !validatorService.isValidPassword(form.getNewPassword()) ) {
            throw new BusinessException(
                    MessageUtil.getErrorMessageInputValues(),
                    BusinessException.BUSINESS_MESSAGE,
                    AppErrorType.INVALID_INPUT
            );
        }
    }
    
}
