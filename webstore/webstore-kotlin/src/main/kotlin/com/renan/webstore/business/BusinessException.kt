package com.renan.webstore.business

import com.renan.webstore.dto.ErrorDTO
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

class BusinessException: RuntimeException {

    private val businessMessage: Boolean
    private val errorType: AppErrorType

    constructor(message: String) : this(message, false, AppErrorType.INTERN_ERROR)

    constructor(message: String, businessMessage: Boolean) : this(message, businessMessage, AppErrorType.INTERN_ERROR)

    constructor(message: String, businessMessage: Boolean, errorType: AppErrorType): super(message) {
        this.businessMessage = businessMessage
        this.errorType = errorType
    }

    fun generateResponseEntity(): ResponseEntity<ErrorDTO> {
        return when (this.errorType) {
            AppErrorType.INVALID_INPUT -> {
                ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body<ErrorDTO?>(ErrorDTO(message?:""))
            }
            AppErrorType.NOT_ALLOWED -> {
                ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body<ErrorDTO?>(ErrorDTO(message?:""))
            }
            else -> {
                ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body<ErrorDTO?>(ErrorDTO(message?:""))
            }
        }
    }

}