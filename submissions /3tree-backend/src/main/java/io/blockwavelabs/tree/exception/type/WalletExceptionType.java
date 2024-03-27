package io.blockwavelabs.tree.exception.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum WalletExceptionType implements ErrorCode{

    WALLET_ALREADY_EXIST("WALLET_ALREADY_EXIST","유저에게 해당 지갑이 이미 존재합니다.", HttpStatus.BAD_REQUEST),
    USER_NOT_MATCHED("USER_NOT_MATCHED", "유저에게 접근 권한이 없는 데이터입니다.", HttpStatus.UNAUTHORIZED),
    NOT_FOUND_INFO("NOT_FOUND_INFO","정보를 찾을 수 없습니다.", HttpStatus.BAD_REQUEST);


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
