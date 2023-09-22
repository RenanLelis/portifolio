package com.renan.webstore.business.impl;

import com.renan.webstore.business.*;
import com.renan.webstore.controller.form.auth.*;
import com.renan.webstore.dto.UserDTO;
import com.renan.webstore.dto.mapper.UserDTOMapper;
import com.renan.webstore.model.AppUser;
import com.renan.webstore.persistence.repository.UserRepository;
import com.renan.webstore.util.MessageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final UserDTOMapper userDTOMapper;
    private final ValidatorService validatorService;
    private final MailService mailService;
    
    public UserDTO login(LoginForm form) throws BusinessException {
        validateLoginForm(form);
        Optional<AppUser> userOpt = repository.findByEmail(form.getEmail().trim().toLowerCase());
        if (userOpt.isEmpty()) throw new BusinessException(
                MessageUtil.getErrorMessageUserNotFound(),
                BusinessException.BUSINESS_MESSAGE,
                AppErrorType.INVALID_INPUT
        );
        if (!passwordEncoder.matches(form.getPassword().trim(), userOpt.get().getPassword()))
            throw new BusinessException(
                    MessageUtil.getErrorMessageLogin(),
                    BusinessException.BUSINESS_MESSAGE,
                    AppErrorType.INVALID_INPUT
            );
        
        return userDTOMapper.apply(userOpt.get());
    }
    
    public void recoverPassword(RecoverPasswordForm form) throws BusinessException {
        validateRecoverPasswordForm(form);
        Optional<AppUser> userOpt = repository.findByEmail(form.getEmail().trim().toLowerCase());
        if (userOpt.isEmpty()) throw new BusinessException(
                MessageUtil.getErrorMessageUserNotFound(),
                BusinessException.BUSINESS_MESSAGE,
                AppErrorType.INVALID_INPUT
        );
        AppUser user = userOpt.get();
        user.setNewPasswordCode(generateNewPasswordCode());
        repository.save(user);
        mailService.sendNewPasswordEmail(user.getEmail(), user.getNewPasswordCode());
    }
    
    public UserDTO resetPasswordByCode(PasswordResetForm form) throws BusinessException {
        validatePasswordResetForm(form);
        Optional<AppUser> userOpt = repository.findByEmail(form.getEmail().trim().toLowerCase());
        if (userOpt.isEmpty()) throw new BusinessException(
                MessageUtil.getErrorMessageUserNotFound(),
                BusinessException.BUSINESS_MESSAGE,
                AppErrorType.INVALID_INPUT
        );
        AppUser user = userOpt.get();
        if (!user.getNewPasswordCode().equals(form.getNewPasswordCode().trim())) throw new BusinessException(
                MessageUtil.getErrorMessageInputValues(),
                BusinessException.BUSINESS_MESSAGE,
                AppErrorType.INVALID_INPUT
        );
        user.setNewPasswordCode(null);
        user.setActive(true);
        user.setPassword(passwordEncoder.encode(form.getPassword()));
        repository.save(user);
        return userDTOMapper.apply(user);
    }
    
    public void registerUser(UserRegistrationForm form) throws BusinessException {
        validateUserRegistrationForm(form);
        Optional<AppUser> userOpt = repository.findByEmail(form.getEmail().trim().toLowerCase());
        if (userOpt.isPresent()) throw new BusinessException(
                MessageUtil.getErrorMessageEmailAlreadyExists(),
                BusinessException.BUSINESS_MESSAGE,
                AppErrorType.INVALID_INPUT
        );
        AppUser newUser = new AppUser();
        newUser.setEmail(form.getEmail().trim().toLowerCase());
        newUser.setPassword(passwordEncoder.encode(form.getPassword().trim()));
        List<String> roles = new ArrayList<>();
        roles.add(AppUser.ROLE_PUBLIC);
        newUser.setRoles(newUser.buildFieldRoles(roles));
        newUser.setActive(false);
        newUser.setFirstName(form.getFirstName().trim());
        newUser.setFirstName(form.getLastName() != null ? form.getLastName().trim() : null);
        newUser.setActivationCode(generateActivationCode());
        repository.save(newUser);
    }
    
    public UserDTO activateUser(UserActivationForm form) throws BusinessException {
        validateUserActivationForm(form);
        Optional<AppUser> userOpt = repository.findByEmail(form.getEmail().trim().toLowerCase());
        if (userOpt.isEmpty()) throw new BusinessException(
                MessageUtil.getErrorMessageUserNotFound(),
                BusinessException.BUSINESS_MESSAGE,
                AppErrorType.INVALID_INPUT
        );
        AppUser user = userOpt.get();
        if (!user.getActivationCode().equals(form.getActivationCode().trim())) throw new BusinessException(
                MessageUtil.getErrorMessageInputValues(),
                BusinessException.BUSINESS_MESSAGE,
                AppErrorType.INVALID_INPUT
        );
        user.setActivationCode(null);
        user.setActive(true);
        repository.save(user);
        return userDTOMapper.apply(user);
    }
    
    public void requestUserActivation(RequestUserActivationForm form) throws BusinessException {
        validateRequestUserActivationForm(form);
        Optional<AppUser> userOpt = repository.findByEmail(form.getEmail().trim().toLowerCase());
        if (userOpt.isEmpty()) throw new BusinessException(
                MessageUtil.getErrorMessageUserNotFound(),
                BusinessException.BUSINESS_MESSAGE,
                AppErrorType.INVALID_INPUT
        );
        AppUser user = userOpt.get();
        user.setActivationCode(generateActivationCode());
        repository.save(user);
        mailService.sendActivationEmail(user.getEmail(), user.getActivationCode());
    }
    
    private void validateLoginForm(LoginForm form) throws BusinessException {
        if (form == null ||
                !validatorService.isMail(form.getEmail()) ||
                !validatorService.isValidPassword(form.getPassword()))
            throw new BusinessException(
                    MessageUtil.getErrorMessageInputValues(),
                    BusinessException.BUSINESS_MESSAGE,
                    AppErrorType.INVALID_INPUT
            );
    }
    
    private void validateRecoverPasswordForm(RecoverPasswordForm form) throws BusinessException {
        if (form == null || !validatorService.isMail(form.getEmail()))
            throw new BusinessException(
                    MessageUtil.getErrorMessageInputValues(),
                    BusinessException.BUSINESS_MESSAGE,
                    AppErrorType.INVALID_INPUT
            );
    }
    
    private void validatePasswordResetForm(PasswordResetForm form) throws BusinessException {
        if (form == null ||
                !validatorService.isMail(form.getEmail()) ||
                !validatorService.isValidPassword(form.getPassword()) ||
                !validatorService.isValidNewPasswordCode(form.getNewPasswordCode()))
            throw new BusinessException(
                    MessageUtil.getErrorMessageInputValues(),
                    BusinessException.BUSINESS_MESSAGE,
                    AppErrorType.INVALID_INPUT
            );
    }
    
    private void validateUserRegistrationForm(UserRegistrationForm form) throws BusinessException {
        if (form == null ||
                !validatorService.isMail(form.getEmail()) ||
                !validatorService.isValidPassword(form.getPassword()) ||
                form.getFirstName() == null || form.getFirstName().isBlank())
            throw new BusinessException(
                    MessageUtil.getErrorMessageInputValues(),
                    BusinessException.BUSINESS_MESSAGE,
                    AppErrorType.INVALID_INPUT
            );
    }
    
    private void validateUserActivationForm(UserActivationForm form) throws BusinessException {
        if (form == null ||
                !validatorService.isMail(form.getEmail()) ||
                !validatorService.isValidActivationCode(form.getActivationCode()))
            throw new BusinessException(
                    MessageUtil.getErrorMessageInputValues(),
                    BusinessException.BUSINESS_MESSAGE,
                    AppErrorType.INVALID_INPUT
            );
    }
    
    private void validateRequestUserActivationForm(RequestUserActivationForm form) throws BusinessException {
        if (form == null ||
                !validatorService.isMail(form.getEmail()))
            throw new BusinessException(
                    MessageUtil.getErrorMessageInputValues(),
                    BusinessException.BUSINESS_MESSAGE,
                    AppErrorType.INVALID_INPUT
            );
    }
    
    private String generateActivationCode() {
        return generateRandomString(AppUser.LENGTH_ACTIVATION_CODE);
    }
    
    private String generateNewPasswordCode() {
        return generateRandomString(AppUser.LENGTH_NEW_PASSWORD_CODE);
    }
    
    private String generateRandomString(Integer length) {
        char[] chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789".toCharArray();
        Random random = new Random();
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int r = random.nextInt(chars.length - 1);
            result.append(chars[r]);
        }
        return result.toString();
    }
    
}
