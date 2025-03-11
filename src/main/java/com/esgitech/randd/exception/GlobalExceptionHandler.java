package com.esgitech.randd.exception;

import com.esgitech.randd.dtos.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({NotFoundException.class, NameValueRequiredException.class, InvalidCredentialsException.class})
    public ResponseEntity<Response> handleCustomExceptions(Exception ex) {
        HttpStatus status;
        if(ex instanceof NotFoundException){
            status = HttpStatus.NOT_FOUND;
        }
        else if(ex instanceof InvalidCredentialsException) {
            status = HttpStatus.UNAUTHORIZED;
        }
        else if(ex instanceof NameValueRequiredException ) {
            status = HttpStatus.BAD_REQUEST;
        }
        else{
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        Response response = Response.builder()
                .status(status.value())
                .message(ex.getMessage())
                .build();

        return new ResponseEntity<>(response, status);
    }
}
