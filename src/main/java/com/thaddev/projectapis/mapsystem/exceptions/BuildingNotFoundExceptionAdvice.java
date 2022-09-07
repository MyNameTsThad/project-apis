package com.thaddev.projectapis.mapsystem.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

public class BuildingNotFoundExceptionAdvice {
    @ResponseBody
    @ExceptionHandler(BuildingNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String buildingNotFoundHandler(BuildingNotFoundException ex) {
        return ex.getMessage();
    }
}
