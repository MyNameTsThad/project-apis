package com.thaddev.projectapis.mapsystem.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

public class POINotFoundExceptionAdvice {
    @ResponseBody
    @ExceptionHandler(POINotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String poiNotFoundHandler(POINotFoundException ex) {
        return ex.getMessage();
    }
}
