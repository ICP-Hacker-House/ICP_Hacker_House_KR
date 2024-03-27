package io.blockwavelabs.tree.exception.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum TokenSendExceptionType implements ErrorCode {
    USER_NOT_MATCHED("USER_NOT_MATCHED", "유저에게 접근 권한이 없는 데이터입니다.", HttpStatus.UNAUTHORIZED),
    NOT_EXISTS_TXN("NOT_EXISTS_TXN", "존재하지 않는 send transaction입니다.", HttpStatus.NOT_FOUND);

    private final String errorCode;
    private final String message;
    private final HttpStatus httpStatus;

    @Override
    public String getErrorCode() {
        return this.errorCode;
    }

    @Override
    public String getMessage() {
        return this.message;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return this.httpStatus;
    }
}