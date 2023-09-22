package com.renan.webstore.controller;

import com.renan.webstore.business.AppErrorType;
import com.renan.webstore.business.BusinessException;
import com.renan.webstore.business.JwtService;
import com.renan.webstore.business.UserService;
import com.renan.webstore.controller.form.user.ChangePasswordForm;
import com.renan.webstore.util.MessageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController("/api/user/")
@RequiredArgsConstructor
public class UserController {
    
    private JwtService jwtService;
    private UserService userService;
    
    @PutMapping("/password")
    public void updatePassword(@RequestBody ChangePasswordForm form, @RequestHeader(JwtService.AUTH) String authorization) {
        Integer userID = jwtService.getIdUser(authorization);
        if (userID == null || userID <= 0) throw new BusinessException(
                MessageUtil.getErrorMessageToken(),
                BusinessException.BUSINESS_MESSAGE,
                AppErrorType.NOT_ALLOWED
        );
        userService.updateUserPassword(form, userID);
    }
    
}
