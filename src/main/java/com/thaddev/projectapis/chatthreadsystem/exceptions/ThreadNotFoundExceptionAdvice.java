package com.thaddev.projectapis.chatthreadsystem.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ThreadNotFoundExceptionAdvice {
    @ResponseBody
    @ExceptionHandler(ThreadNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public String threadNotFoundHandler(ThreadNotFoundException ex) {
        return ex.getMessage();
    }
}
