package com.thaddev.projectapis.computercontrolsystem.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class EmptyCommandExceptionAdvice {
    @ResponseBody
    @ExceptionHandler(EmptyCommandException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String emptyCommandHandler(EmptyCommandException ex){
        return ex.getMessage();
    }
}
