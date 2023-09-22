package com.renan.webstore.controller;

import com.renan.webstore.business.BusinessException;
import com.renan.webstore.dto.ErrorDTO;
import com.renan.webstore.util.MessageUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class AppExceptionHandler {
    
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorDTO> handleBusinessExceptions(BusinessException e) {
        return e.getResponseEntity();
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDTO> handleExceptions(Exception e) {
//        e.printStackTrace();
        if (e instanceof BusinessException) {
            return ((BusinessException) e).getResponseEntity();
        }
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorDTO(MessageUtil.getErrorMessage()));
    }
    
}

