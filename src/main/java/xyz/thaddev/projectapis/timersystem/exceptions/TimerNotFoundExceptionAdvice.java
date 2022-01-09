package xyz.thaddev.projectapis.timersystem.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class TimerNotFoundExceptionAdvice {
    @ResponseBody
    @ExceptionHandler(TimerNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String timerNotFoundHandler(TimerNotFoundException ex){
        return ex.getMessage();
    }
}
