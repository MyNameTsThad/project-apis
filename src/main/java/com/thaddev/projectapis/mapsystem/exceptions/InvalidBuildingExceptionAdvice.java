package com.thaddev.projectapis.mapsystem.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

public class InvalidBuildingExceptionAdvice {
    @ResponseBody
    @ExceptionHandler(InvalidBuildingException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String invalidBuildingHandler(InvalidBuildingException ex) {
        return ex.getMessage();
    }
}
