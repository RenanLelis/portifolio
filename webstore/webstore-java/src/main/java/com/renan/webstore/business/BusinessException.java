package com.renan.webstore.business;

import com.renan.webstore.dto.ErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class BusinessException extends RuntimeException {
    
    public static final Boolean BUSINESS_MESSAGE = true;
    private Boolean businessMessage = Boolean.FALSE;
    private AppErrorType errorType;
    
    public BusinessException(String message) {
        this(message, false);
    }
    
    public BusinessException(String message, Boolean businessMessage) {
        this(message, businessMessage, AppErrorType.INTERN_ERROR);
    }
    
    public BusinessException(String message, Boolean businessMessage, AppErrorType errorType) {
        super(message);
        this.businessMessage = businessMessage;
        this.errorType = errorType;
    }
    
    public AppErrorType getErrorType() {
        return errorType;
    }
    
    public void setErrorType(AppErrorType errorType) {
        this.errorType = errorType;
    }
    
    public Boolean isBusinessMessage() {
        return businessMessage;
    }
    
    public ResponseEntity<ErrorDTO> getResponseEntity() {
        if (this.getErrorType().equals(AppErrorType.INVALID_INPUT)) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorDTO(this.getMessage()));
        } else if (this.getErrorType().equals(AppErrorType.NOT_ALLOWED)) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorDTO(this.getMessage()));
        } else {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorDTO(this.getMessage()));
        }
    }
    
}
