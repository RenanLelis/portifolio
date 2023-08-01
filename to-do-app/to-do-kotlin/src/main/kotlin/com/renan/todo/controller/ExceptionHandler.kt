package com.renan.todo.controller

import com.renan.todo.dto.ErrorDTO
import com.renan.todo.service.BusinessException
import com.renan.todo.util.getErrorMessage
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class ExceptionHandler {

    /**
     * Handle de business exceptions and return the response entity with the status code
     */
    @ExceptionHandler(BusinessException::class)
    fun handleBusinessExceptions(e: BusinessException) = e.generateResponseEntity()

    /**
     * Handle exceptions and return the response entity with the status code
     */
    @ExceptionHandler(Exception::class)
    fun handleExceptions(e: Exception): ResponseEntity<ErrorDTO> {
        e.printStackTrace()
        return if (e is BusinessException) {
            e.generateResponseEntity()
        } else {
            ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorDTO(getErrorMessage()))
        }
    }

}