package com.springboot.advice;

import com.springboot.exception.BusinessLogicException;
import com.springboot.exception.ExceptionCode;
import com.springboot.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Set;

@RestControllerAdvice
public class GlobalExceptionAdvice {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMethodArgumentNotValidException(
            MethodArgumentNotValidException e) {
        final ErrorResponse response = ErrorResponse.of(e.getBindingResult());

        return response;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleConstraintViolationException(
            ConstraintViolationException e) {
        final ErrorResponse response = ErrorResponse.of(e.getConstraintViolations());

        return response;
    }

    @ExceptionHandler
    public ResponseEntity handleBusinessLogicException(BusinessLogicException e) {
        System.out.println(e.getExceptionCode().getStatus());
        System.out.println(e.getMessage());

        // TODO GlobalExceptionAdvice 기능 추가 1

        final ErrorResponse response = ErrorResponse.of(e.getExceptionCode());
        return new ResponseEntity<>(response, HttpStatus.valueOf(e.getExceptionCode()
                .getStatus()));
    }

    // TODO GlobalExceptionAdvice 기능 추가 2

    @ExceptionHandler
    public ErrorResponse handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {

        return ErrorResponse.of(e);
    }
    // TODO GlobalExceptionAdvice 기능 추가 3

    @ExceptionHandler
    public ErrorResponse handleException(NullPointerException e){
        //최종 이해한,,, of가 객체를 만들어, ErrorResponse에 of가 만든 객체를 인자로 받은 생성자의 반환값이 들어와.

//        return ErrorResponse.of(ExceptionCode.valueOf(e.getMessage()), ExceptionCode.values()) ;
//        return ErrorResponse.of(response, HttpStatus.INTERNAL_SERVER_ERROR) ;
        return ErrorResponse.of(e);
    }

}
