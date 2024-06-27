package org.enrollment.lecture.infra.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    CONTENT_NOT_FOUND(HttpStatus.GONE, "CONTENT_NOT_FOUND"),
    CONFLICT(HttpStatus.CONFLICT, "CONFLICT"),

    USER_EXISTED(HttpStatus.CONFLICT, "USER_EXISTED"),

    ;

    public final HttpStatus httpStatus;
    public final String message;

}
