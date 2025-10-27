package com.api.library.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

import com.api.library.dto.ResponseWrapper;

import jakarta.validation.ConstraintViolationException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ResponseWrapper> handleResponseStatusException(ResponseStatusException ex) {
        ResponseWrapper response = new ResponseWrapper();
        response.setStatus("error");
        response.setMessage(ex.getReason());
        return new ResponseEntity<>(response, ex.getStatusCode());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseWrapper> handleValidationExceptions(MethodArgumentNotValidException ex) {
        ResponseWrapper response = new ResponseWrapper();
        response.setStatus("error");
        response.setMessage("Validation error: " + 
            ex.getBindingResult().getAllErrors().get(0).getDefaultMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ResponseWrapper> handleConstraintViolation(ConstraintViolationException ex) {
        ResponseWrapper response = new ResponseWrapper();
        response.setStatus("error");
        response.setMessage("Validation error: " + ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseWrapper> handleGenericException(Exception ex) {
        ResponseWrapper response = new ResponseWrapper();
        response.setStatus("error");
        response.setMessage("An unexpected error occurred");
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
