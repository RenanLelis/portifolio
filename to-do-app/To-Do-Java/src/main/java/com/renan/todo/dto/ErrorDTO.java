package com.renan.todo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Transfer Object for Error Message
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ErrorDTO {
    private String errorMessage;

}