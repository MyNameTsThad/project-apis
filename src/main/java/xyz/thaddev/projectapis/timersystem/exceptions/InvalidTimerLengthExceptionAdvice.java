package xyz.thaddev.projectapis.timersystem.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class InvalidTimerLengthExceptionAdvice {
    @ResponseBody
    @ExceptionHandler(InvalidTimerLengthException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String invalidTimerLengthHandler(InvalidTimerLengthException ex){
        return ex.getMessage();
    }
}
