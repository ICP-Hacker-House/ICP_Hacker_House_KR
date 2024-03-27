package io.blockwavelabs.tree.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum ErrorStatus {
    TWITTER_TOKEN_INVALID(HttpStatus.UNAUTHORIZED.value(), "검증할 수 없는 트위터 OAuth 2.0 토큰입니다."),
    ;

    private final int code;
    private final String message;
}
