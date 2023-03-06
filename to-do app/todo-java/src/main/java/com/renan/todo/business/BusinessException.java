package com.renan.todo.business;

public class BusinessException extends Exception {
    private Boolean             businessMessage  = Boolean.FALSE;
    public static final Boolean BUSINESS_MESSAGE = true;
    private AppErrorType            errorType;

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
}
