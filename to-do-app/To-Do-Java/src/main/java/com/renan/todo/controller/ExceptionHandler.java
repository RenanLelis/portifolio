package com.renan.todo.controller;

import com.renan.todo.dto.ErrorDTO;
import com.renan.todo.service.BusinessException;
import com.renan.todo.util.MessageUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class ExceptionHandler {
    
    /**
     * Handle de business exceptions and return the response entity with the status code
     *
     * @param e - the exception
     * @return the response entity with the status code and error dto
     */
    @org.springframework.web.bind.annotation.ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorDTO> handleBusinessExceptions(BusinessException e) {
        return e.getResponseEntity();
    }
    
    /**
     * Handle exceptions and return the response entity with the status code
     *
     * @param e - the exception
     * @return the response entity with the status code and error dto
     */
    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDTO> handleExceptions(Exception e) {
        e.printStackTrace();
        if (e instanceof BusinessException) {
            return ((BusinessException) e).getResponseEntity();
        }
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorDTO(MessageUtil.getErrorMessage()));
    }
    
}
