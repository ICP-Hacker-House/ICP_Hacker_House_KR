package io.blockwavelabs.tree.exception.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserExceptionType implements ErrorCode {

    NOT_FOUND_USER("NOT_FOUND_USER","사용자를 찾을 수 없습니다.", HttpStatus.BAD_REQUEST),
    NOT_FOUND_INFO("NOT_FOUND_INFO","정보를 찾을 수 없습니다.", HttpStatus.BAD_REQUEST),
    ILLEGAL_REQUEST("ILLEGAL_REQUEST","데이터의 소유가 아닙니다.", HttpStatus.BAD_REQUEST),
    DUPLICATE_USER("DUPLICATE_USER","이미 존재하는 사용자입니다.", HttpStatus.BAD_REQUEST),
    DUPLICATE_ID("DUPLICATE_ID","이미 존재하는 ID 입니다.", HttpStatus.BAD_REQUEST),
    INVALID_USER("INVALID_USER","유효하지 않은 사용자입니다.", HttpStatus.BAD_REQUEST),
    WRONG_PASSWORD("WRONG_PASSWORD","비밀번호를 잘못 입력하였습니다.", HttpStatus.UNAUTHORIZED),
    NOT_FOUND_TOKEN("NOT_FOUND_TOKEN","TOKEN 정보가 없습니다.",HttpStatus.BAD_REQUEST),
    LOGOUT_MEMBER("LOGOUT_MEMBER","로그아웃된 사용자입니다.",HttpStatus.BAD_REQUEST),
    JWT_EXPIRED("JWT_EXPIRED","ACCESS_TOKEN 이 만료되었습니다.", HttpStatus.UNAUTHORIZED)
    ;



    private final String errorCode;
    private final String message;
    private final HttpStatus httpStatus;

}