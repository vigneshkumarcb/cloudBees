package com.train.services.ticketing.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.train.services.ticketing.model.CustomizedError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {

    private final ObjectMapper objectMapper;

    @Autowired
    public GlobalExceptionHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<String> handleException(Exception ex) throws JsonProcessingException {
        return new ResponseEntity<>(objectMapper.writeValueAsString(
                new CustomizedError(1001, "Internal Server Error: " + ex.getMessage())), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException ex) throws JsonProcessingException {
        return new ResponseEntity<>(objectMapper.writeValueAsString(
                new CustomizedError(1002, "User Not Found: " + ex.getMessage())), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(SeatsNotAvailableException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<String> handleSeatsNotAvailableException(SeatsNotAvailableException ex) throws JsonProcessingException {
        return new ResponseEntity<>(objectMapper.writeValueAsString(
                new CustomizedError(1003, "Seats not available: " + ex.getMessage())), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UserListNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> handleUserListNotFoundException(UserListNotFoundException ex) throws JsonProcessingException {
        return new ResponseEntity<>(objectMapper.writeValueAsString(
                new CustomizedError(1004, "User List is empty: " + ex.getMessage())), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(SameUserBookingException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<String> handleUserListNotFoundException(SameUserBookingException ex) throws JsonProcessingException {
        return new ResponseEntity<>(objectMapper.writeValueAsString(
                new CustomizedError(1005, "User Id is already present: " + ex.getMessage())), HttpStatus.CONFLICT);
    }

    public static class SameUserBookingException extends RuntimeException {
        public SameUserBookingException(String message) {
            super(message);
        }
    }

    public static class UserNotFoundException extends RuntimeException {
        public UserNotFoundException(String message) {
            super(message);
        }
    }

    public static class UserListNotFoundException extends RuntimeException {
        public UserListNotFoundException(String message) {
            super(message);
        }
    }

    public static class SeatsNotAvailableException extends RuntimeException {
        public SeatsNotAvailableException(String message) {
            super(message);
        }
    }
}

