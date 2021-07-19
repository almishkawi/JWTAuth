package com.mo.JWTAuth.exception;

import com.mo.JWTAuth.error.UserError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import javax.validation.ConstraintViolationException;
import java.util.*;

@ControllerAdvice
public class ExceptionsHandler {
    @ExceptionHandler
    public ResponseEntity<Set<String>> handleException(Exception ex, WebRequest request) {
        UserError userError = new UserError();
        userError.addError(ex.getMessage());

        return new ResponseEntity(userError.getErrors(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Set<String>> handleValidationExceptions(ConstraintViolationException ex, WebRequest request) {
        UserError userError = new UserError();
        ex.getConstraintViolations().forEach((error) -> {
            String errorMessage = error.getMessage();
            userError.addError(errorMessage);
        });

        return new ResponseEntity(userError.getErrors(), HttpStatus.BAD_REQUEST);
    }
}
