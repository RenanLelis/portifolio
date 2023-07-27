package com.renan.webstore.service.validator;

import com.renan.webstore.model.User;
import com.renan.webstore.service.AppErrorType;
import com.renan.webstore.service.BusinessException;
import com.renan.webstore.util.MessageUtil;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class AuthValidatorImpl implements AuthValidator {
    
    public void validateLoginInput(String email, String password) throws BusinessException {
        if (!isEmailValid(email) || !isPasswordRequirementsValid(password)) {
            throw new BusinessException(
                    MessageUtil.getErrorMessageInputValues(),
                    BusinessException.BUSINESS_MESSAGE,
                    AppErrorType.INVALID_INPUT
            );
        }
    }
    
    public void validateNewUserInput(String email, String password, String name, String lastName) throws BusinessException {
        if (!isEmailValid(email) || !isPasswordRequirementsValid(password) || name == null || name.trim().length() == 0) {
            throw new BusinessException(
                    MessageUtil.getErrorMessageInputValues(),
                    BusinessException.BUSINESS_MESSAGE,
                    AppErrorType.INVALID_INPUT
            );
        }
    }
    
    public void validateResetPasswordByCodeInput(String email, String newPassword, String newPasswordCode) throws BusinessException {
        if (!isEmailValid(email) || !isNewpasswordCodeValid(newPasswordCode) || !isPasswordRequirementsValid(newPassword)) {
            throw new BusinessException(
                    MessageUtil.getErrorMessageInputValues(),
                    BusinessException.BUSINESS_MESSAGE,
                    AppErrorType.INVALID_INPUT
            );
        }
    }
    
    public void validateUserActivationInput(String email, String activationCode) throws BusinessException {
        if (!isEmailValid(email) || !isActivationCodeValid(activationCode)) {
            throw new BusinessException(
                    MessageUtil.getErrorMessageInputValues(),
                    BusinessException.BUSINESS_MESSAGE,
                    AppErrorType.INVALID_INPUT
            );
        }
    }
    
    public void validateRequestUserActivationInput(String email) throws BusinessException {
        if (!isEmailValid(email)) {
            throw new BusinessException(
                    MessageUtil.getErrorMessageInputValues(),
                    BusinessException.BUSINESS_MESSAGE,
                    AppErrorType.INVALID_INPUT
            );
        }
    }
    
    public void validateRecoverPasswordInput(String email) throws BusinessException {
        if (!isEmailValid(email)) {
            throw new BusinessException(
                    MessageUtil.getErrorMessageInputValues(),
                    BusinessException.BUSINESS_MESSAGE,
                    AppErrorType.INVALID_INPUT
            );
        }
    }
    
    public boolean isEmailValid(String email) {
        if (email != null && email.trim().length() > 0) {
            String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
            Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(email);
            return matcher.matches();
        }
        return false;
    }
    
    public boolean isPasswordRequirementsValid(String password) {
        return password != null &&
                password.trim().length() >= User.MIN_LENGTH_PASSWORD;
    }
    
    public boolean isNewpasswordCodeValid(String newPasswordCode) {
        return newPasswordCode != null &&
                newPasswordCode.trim().length() == User.LENGTH_NEW_PASSWORD_CODE;
    }
    
    public boolean isActivationCodeValid(String activationCode) {
        return activationCode != null &&
                activationCode.trim().length() == User.LENGTH_ACTIVATION_CODE;
    }
    
}
