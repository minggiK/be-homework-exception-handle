package com.springboot.response;

import com.springboot.exception.ExceptionCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpRequestMethodNotSupportedException;

import javax.validation.ConstraintViolation;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Getter
public class ErrorResponse {
    private List<FieldError> fieldErrors;
    private List<ConstraintViolationError> violationErrors;
    private String message;
    private int status;

    private ErrorResponse(String message,
                          int status) {

        this.message = message;
        this.status = status;
    }
    private ErrorResponse(final List<FieldError> fieldErrors,
                          final List<ConstraintViolationError> violationErrors) {
        this.fieldErrors = fieldErrors;
        this.violationErrors = violationErrors;
    }


//    private ExceptionCode exceptionCodes;
//
//    private ErrorResponse(final List<FieldError> fieldErrors,
//                          final List<ConstraintViolationError> violationErrors,
//                          final ExceptionCode exceptionCodes) {
//
//        this.fieldErrors = fieldErrors;
//        this.violationErrors = violationErrors;
//        this.exceptionCodes = exceptionCodes;
//    }


    public static ErrorResponse of(BindingResult bindingResult) {
        return new ErrorResponse(FieldError.of(bindingResult), null);
    }

    public static ErrorResponse of(Set<ConstraintViolation<?>> violations) {
        return new ErrorResponse(null, ConstraintViolationError.of(violations));
    }

    public static ErrorResponse of(ExceptionCode exceptionCode) {
        return new ErrorResponse(exceptionCode.getMessage(), exceptionCode.getStatus());
    }

    public static ErrorResponse of(HttpRequestMethodNotSupportedException e) {

        return new ErrorResponse(HttpStatus.METHOD_NOT_ALLOWED.getReasonPhrase(), HttpStatus.METHOD_NOT_ALLOWED.value());
    }

    public static ErrorResponse of(NullPointerException e) {
        //ExceptionCode 를 불러오지 않아서 에러
        return new ErrorResponse(ExceptionCode.INTERNAL_SERVER_ERROR.getMessage(), ExceptionCode.INTERNAL_SERVER_ERROR.getStatus());

    }



    @Getter
    public static class FieldError {
        private String field;
        private Object rejectedValue;
        private String reason;

        private FieldError(String field, Object rejectedValue, String reason) {
            this.field = field;
            this.rejectedValue = rejectedValue;
            this.reason = reason;
        }

        public static List<FieldError> of(BindingResult bindingResult) {
            final List<org.springframework.validation.FieldError> fieldErrors =
                                                        bindingResult.getFieldErrors();
            return fieldErrors.stream()
                    .map(error -> new FieldError(
                            error.getField(),
                            error.getRejectedValue() == null ?
                                            "" : error.getRejectedValue().toString(),
                            error.getDefaultMessage()))
                    .collect(Collectors.toList());
        }
    }

    @Getter
    public static class ConstraintViolationError {
        private String propertyPath;
        private Object rejectedValue;
        private String reason;

        private ConstraintViolationError(String propertyPath, Object rejectedValue,
                                   String reason) {
            this.propertyPath = propertyPath;
            this.rejectedValue = rejectedValue;
            this.reason = reason;
        }

        public static List<ConstraintViolationError> of(
                Set<ConstraintViolation<?>> constraintViolations) {
            return constraintViolations.stream()
                    .map(constraintViolation -> new ConstraintViolationError(
                            constraintViolation.getPropertyPath().toString(),
                            constraintViolation.getInvalidValue().toString(),
                            constraintViolation.getMessage()
                    )).collect(Collectors.toList());
        }
    }


}