package xyz.thaddev.projectapis;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

public class PermissionDeniedExceptionAdvice {
    @ResponseBody
    @ExceptionHandler(PermissionDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    String permissionDenied(PermissionDeniedException ex){
        return ex.getMessage();
    }
}
