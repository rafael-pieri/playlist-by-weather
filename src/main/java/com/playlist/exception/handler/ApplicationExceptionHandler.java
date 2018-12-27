package com.playlist.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.playlist.exception.BadRequestException;
import com.playlist.exception.NotFoundException;
import com.playlist.exception.ServiceUnavailableException;
import com.playlist.model.ErrorDetail;

@ControllerAdvice
public class ApplicationExceptionHandler {

    @ExceptionHandler(value = BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorDetail badRequestExceptionHandler(final BadRequestException exception) {
        return new ErrorDetail(exception.getMessage());
    }

    @ExceptionHandler(value = NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorDetail notFoundExceptionHandler(final NotFoundException exception) {
        return new ErrorDetail(exception.getMessage());
    }

    @ExceptionHandler(value = ServiceUnavailableException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    @ResponseBody
    public ErrorDetail serviceUnavailableExceptionHandler(final ServiceUnavailableException exception) {
        return new ErrorDetail(exception.getMessage());
    }
}