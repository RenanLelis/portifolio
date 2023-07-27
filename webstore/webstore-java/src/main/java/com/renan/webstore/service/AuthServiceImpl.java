package com.renan.webstore.service;

import com.renan.webstore.dto.UserDto;
import com.renan.webstore.model.Authority;
import com.renan.webstore.model.Role;
import com.renan.webstore.model.User;
import com.renan.webstore.persistence.UserRepository;
import com.renan.webstore.security.JwtService;
import com.renan.webstore.service.validator.AuthValidator;
import com.renan.webstore.util.MessageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    
    private final JwtService jwtService;
    private final UserRepository repository;
    private final MailService mailService;
    private final AuthValidator validator;
    private final PasswordEncoder passwordEncoder;
    private final UtilService utilService;
    
    public UserDto login(String email, String password) throws BusinessException {
        validator.validateLoginInput(email, password);
        Optional<User> optUser = repository.findByEmail(email.trim());
        if (optUser.isEmpty()) {
            throw new BusinessException(MessageUtil.getErrorMessageUserNotFound(), BusinessException.BUSINESS_MESSAGE, AppErrorType.INVALID_INPUT);
        }
        if (!passwordEncoder.matches(password.trim(), optUser.get().getPassword())) {
            throw new BusinessException(MessageUtil.getErrorMessageLogin(),
                    BusinessException.BUSINESS_MESSAGE,
                    AppErrorType.INVALID_INPUT);
        }
        if (optUser.get().getUserStatus().equals(User.STATUS_INACTIVE)) {
            throw new BusinessException(MessageUtil.getErrorMessageUserNotActive(),
                    BusinessException.BUSINESS_MESSAGE,
                    AppErrorType.INVALID_INPUT);
        }
        List<String> roles = getRolesFromUser(optUser.get());
        String jwt = generateJwt(optUser.get());
        return new UserDto(
                optUser.get().getEmail(),
                jwt,
                optUser.get().getId(),
                optUser.get().getUserStatus(),
                jwtService.getJwtExpiration(),
                optUser.get().getFirstName(),
                optUser.get().getLastName(),
                roles
        );
    }
    
    public void recoverPassword(String email) throws BusinessException {
        validator.validateRecoverPasswordInput(email);
        Optional<User> optUser = repository.findByEmail(email.trim());
        if (optUser.isEmpty()) {
            throw new BusinessException(MessageUtil.getErrorMessageUserNotFound(), BusinessException.BUSINESS_MESSAGE, AppErrorType.INVALID_INPUT);
        }
        User user = optUser.get();
        if (user.getUserStatus().equals(User.STATUS_INACTIVE)) {
            throw new BusinessException(MessageUtil.getErrorMessageUserNotActive(), BusinessException.BUSINESS_MESSAGE, AppErrorType.INVALID_INPUT);
        }
        user.setNewPasswordCode(utilService.generateRandomString());
        repository.save(user);
        mailService.sendNewPasswordEmail(user.getEmail(), user.getNewPasswordCode());
    }
    
    public UserDto resetPasswordByCode(String email, String newPassword, String newPasswordCode) throws BusinessException {
        validator.validateResetPasswordByCodeInput(email, newPassword, newPasswordCode);
        Optional<User> optUser = repository.findByEmail(email.trim());
        if (optUser.isEmpty()) {
            throw new BusinessException(MessageUtil.getErrorMessageUserNotFound(), BusinessException.BUSINESS_MESSAGE, AppErrorType.INVALID_INPUT);
        }
        User user = optUser.get();
        if (!user.getNewPasswordCode().equals(newPasswordCode.trim())) {
            throw new BusinessException(MessageUtil.getErrorMessageInputValues(), BusinessException.BUSINESS_MESSAGE, AppErrorType.INVALID_INPUT);
        }
        user.setNewPasswordCode(null);
        user.setUserStatus(User.STATUS_ACTIVE);
        user.setPassword(passwordEncoder.encode(newPassword.trim()));
        repository.save(user);
        List<String> roles = getRolesFromUser(user);
        String jwt = generateJwt(user);
        return new UserDto(
                user.getEmail(),
                jwt,
                user.getId(),
                user.getUserStatus(),
                jwtService.getJwtExpiration(),
                user.getFirstName(),
                user.getLastName(),
                roles
        );
    }
    
    public void registerUser(String email, String password, String name, String lastName) throws BusinessException {
        validator.validateNewUserInput(email, password, name, lastName);
        Optional<User> optUser = repository.findByEmail(email.trim());
        if (optUser.isPresent()) {
            throw new BusinessException(MessageUtil.getErrorMessageEmailAlreadyExists(), BusinessException.BUSINESS_MESSAGE, AppErrorType.INVALID_INPUT);
        }
        String activationCode = utilService.generateRandomString();
        User user = new User(
                null,
                name.trim(),
                lastName != null ? lastName.trim() : "",
                email.trim(),
                passwordEncoder.encode(password),
                activationCode,
                null,
                User.STATUS_INACTIVE,
                Role.PUBLIC.getName());
        repository.save(user);
        mailService.sendActivationEmail(email.trim(), activationCode);
    }
    
    public UserDto activateUser(String email, String activationCode) throws BusinessException {
        validator.validateUserActivationInput(email, activationCode);
        Optional<User> optUser = repository.findByEmail(email);
        if (optUser.isEmpty()) {
            throw new BusinessException(MessageUtil.getErrorMessageUserNotFound(), BusinessException.BUSINESS_MESSAGE, AppErrorType.INVALID_INPUT);
        }
        User user = optUser.get();
        if (!user.getActivationCode().equals(activationCode)) {
            throw new BusinessException(
                    MessageUtil.getErrorMessageInputValues(),
                    BusinessException.BUSINESS_MESSAGE,
                    AppErrorType.INVALID_INPUT
            );
        }
        user.setUserStatus(User.STATUS_ACTIVE);
        repository.save(user);
        List<String> roles = getRolesFromUser(user);
        String jwt = generateJwt(user);
        return new UserDto(
                user.getEmail(),
                jwt,
                user.getId(),
                user.getUserStatus(),
                jwtService.getJwtExpiration(),
                user.getFirstName(),
                user.getLastName(),
                roles
        );
    }
    
    public void requestUserActivation(String email) throws BusinessException {
        validator.validateRecoverPasswordInput(email);
        Optional<User> optUser = repository.findByEmail(email);
        if (optUser.isEmpty()) {
            throw new BusinessException(MessageUtil.getErrorMessageUserNotFound(), BusinessException.BUSINESS_MESSAGE, AppErrorType.INVALID_INPUT);
        }
        User user = optUser.get();
        user.setActivationCode(utilService.generateRandomString());
        repository.save(user);
        mailService.sendActivationEmail(user.getEmail(), user.getActivationCode());
    }
    
    private List<String> getRolesFromUser(User user) {
        if (user.getUserRoles().contains(",")) {
            return List.of(user.getUserRoles().split(","));
        }
        return List.of(user.getUserRoles());
    }
    
    private String generateJwt(User user) {
        return jwtService.generateJWT(
                user.getId(),
                user.getUserStatus(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                getRolesFromUser(user)
        );
    }
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (validator.isEmailValid(username)) {
            Optional<User> optUser = repository.findByEmail(username);
            if (optUser.isPresent()) {
                User user = optUser.get();
                return new UserDetails() {
                    @Override
                    public Collection<? extends GrantedAuthority> getAuthorities() {
                        List<String> roles = getRolesFromUser(user);
                        List<GrantedAuthority> authorities = new ArrayList<>();
                        for (String role: roles) {
                            authorities.add(new Authority(role));
                        }
                        return authorities;
                    }
                    
                    @Override
                    public String getPassword() {
                        return user.getPassword();
                    }
                    
                    @Override
                    public String getUsername() {
                        return user.getEmail();
                    }
                    
                    @Override
                    public boolean isAccountNonExpired() {
                        return false;
                    }
                    
                    @Override
                    public boolean isAccountNonLocked() {
                        return false;
                    }
                    
                    @Override
                    public boolean isCredentialsNonExpired() {
                        return false;
                    }
                    
                    @Override
                    public boolean isEnabled() {
                        return false;
                    }
                };
            }
        }
        return null;
    }
    
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        return authentication;
    }
    
    @Override
    public boolean supports(Class<?> authentication) {
        return false;
    }
}
