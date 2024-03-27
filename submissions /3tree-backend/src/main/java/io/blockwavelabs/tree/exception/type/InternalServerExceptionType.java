package io.blockwavelabs.tree.exception.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum InternalServerExceptionType implements ErrorCode {
    INTERNAL_SERVER_ERROR("INTERNAL_SERVER_ERROR","서버 내부에 문제가 발생하였습니다.", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String errorCode;
    private final String message;
    private final HttpStatus httpStatus;

}
