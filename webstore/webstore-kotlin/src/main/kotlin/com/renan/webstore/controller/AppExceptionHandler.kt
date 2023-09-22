package com.renan.webstore.controller

import com.renan.webstore.business.BusinessException
import com.renan.webstore.dto.ErrorDTO
import com.renan.webstore.util.getErrorMessage
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class AppExceptionHandler {

    @ExceptionHandler(BusinessException::class)
    fun handleBusinessExceptions(e: BusinessException) = e.generateResponseEntity()

    @ExceptionHandler(Exception::class)
    fun handleExceptions(e: Exception): ResponseEntity<ErrorDTO> {
        e.printStackTrace()
        return when (e) {
            is BusinessException -> e.generateResponseEntity()
            is HttpMessageNotReadableException -> ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorDTO(getErrorMessage()))

            else -> ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorDTO(getErrorMessage()))
        }
    }

}