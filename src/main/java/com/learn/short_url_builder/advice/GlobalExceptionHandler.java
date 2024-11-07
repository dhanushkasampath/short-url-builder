package com.learn.short_url_builder.advice;

import com.learn.short_url_builder.exception.ShortUrlBuilderException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ShortUrlBuilderException.class)//here is the place that throwing exceptions are caught
    public ResponseEntity<Object> handleShortUrlBuilderException(ShortUrlBuilderException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    // add more exception handler methods here.
}
