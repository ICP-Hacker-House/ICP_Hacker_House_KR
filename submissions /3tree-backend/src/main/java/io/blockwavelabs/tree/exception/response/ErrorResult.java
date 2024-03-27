package io.blockwavelabs.tree.exception.response;

import io.blockwavelabs.tree.exception.type.ErrorCode;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

@Getter
@Builder
@RequiredArgsConstructor
public class ErrorResult {
    private final String error;
    private final String code;
    private final String message;
    private final LocalDateTime timestamp = LocalDateTime.now();

    public static ResponseEntity<ErrorResult> toResponseEntity(ErrorCode errorCode){
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(ErrorResult.builder()
                        .error(errorCode.getHttpStatus().toString())
                        .code(errorCode.getErrorCode())
                        .message(errorCode.getMessage())
                        .build());
    }

    public static ResponseEntity<ErrorResult> toResponseEntity(HttpStatus httpStatus, String error, String code, String message){
        return ResponseEntity
                .status(httpStatus)
                .body(ErrorResult.builder()
                        .error(error)
                        .code(code)
                        .message(message)
                        .build());
    }

}
