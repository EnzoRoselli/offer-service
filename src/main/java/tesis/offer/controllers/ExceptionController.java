package tesis.offer.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import tesis.offer.models.ErrorResponse;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ExceptionController {

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ErrorResponse handleUserDoesNotExists(Exception ex) {
        return new ErrorResponse("422",ex.getMessage(), LocalDateTime.now());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        return new ErrorResponse("404",ex.getMessage(), LocalDateTime.now());
    }
}
