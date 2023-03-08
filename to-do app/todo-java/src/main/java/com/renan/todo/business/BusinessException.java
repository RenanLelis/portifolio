package com.renan.todo.business;

import com.renan.todo.dto.ErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class BusinessException extends Exception {
    private Boolean businessMessage = Boolean.FALSE;
    public static final Boolean BUSINESS_MESSAGE = true;
    private AppErrorType errorType;

    public AppErrorType getErrorType() {
        return errorType;
    }

    public void setErrorType(AppErrorType errorType) {
        this.errorType = errorType;
    }

    public BusinessException(String message) {
        super(message);
        this.businessMessage = false;
        this.errorType = AppErrorType.INTERN_ERROR;
    }

    public BusinessException(String message, Boolean businessMessage) {
        super(message);
        this.businessMessage = businessMessage;
        this.errorType = AppErrorType.INTERN_ERROR;
    }

    public BusinessException(String message, Boolean businessMessage, AppErrorType errorType) {
        super(message);
        this.businessMessage = businessMessage;
        this.errorType = errorType;
    }

    public Boolean isBusinessMessage() {
        return businessMessage;
    }

    /**
     * Simplify the response for the request controller
     *
     * @return - the ResponseEntity
     */
    public ResponseEntity getResponseEntity() {
        if (this.getErrorType().equals(AppErrorType.INVALID_INPUT)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDTO(this.getMessage()));
        } else if (this.getErrorType().equals(AppErrorType.NOT_ALLOWED)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorDTO(this.getMessage()));
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorDTO(this.getMessage()));
        }
    }
}
