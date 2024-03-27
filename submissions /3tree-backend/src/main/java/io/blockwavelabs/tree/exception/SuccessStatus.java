package io.blockwavelabs.tree.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum SuccessStatus {

    SOCIAL_CONNECT_SUCCESS(HttpStatus.OK.value(), "소셜 계정 연동에 성공했습니다."),
    SOCIAL_DISCONNECT_SUCCESS(HttpStatus.OK.value(), "소셜 계정 연동 해제에 성공했습니다."),
    GET_SOCIAL_ACCOUNT_LIST_SUCCESS(HttpStatus.OK.value(), "소셜 계정 리스트 조회에 성공했습니다."),

    GET_TWITTER_TOKEN_SUCCESS(HttpStatus.OK.value(), "트위터 토큰 발급에 성공했습니다."),
    POST_TWEET_SUCCESS(HttpStatus.OK.value(), "트윗 생성에 성공했습니다."),

    ;

    private final int code;
    private final String message;


}
