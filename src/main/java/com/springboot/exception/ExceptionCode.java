package com.springboot.exception;

import lombok.Getter;

import javax.validation.ConstraintViolation;
import java.util.List;
import java.util.Set;

public enum ExceptionCode {
    MEMBER_NOT_FOUND(404, "Member Not Found"),
    INTERNAL_SERVER_ERROR(500,"Insternal Server Error");
    @Getter
    private int status;

    @Getter
    private String message;

    ExceptionCode(int status, String message) {
        this.status = status;
        this.message = message;
    }
}
