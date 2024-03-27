package io.blockwavelabs.tree.common.dto;

import io.blockwavelabs.tree.exception.ErrorStatus;
import io.blockwavelabs.tree.exception.SuccessStatus;
import lombok.*;
import org.springframework.http.ResponseEntity;

@ToString
@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiResponseDto<T> {

    private final int code;
    private final String message;
    private T data;

    public static <T> ApiResponseDto<T> success(SuccessStatus successStatus) {
        return new ApiResponseDto<>(successStatus.getCode(), successStatus.getMessage(), null);
    }

    public static <T> ApiResponseDto<T> success(SuccessStatus successStatus, T data) {
        return new ApiResponseDto<>(successStatus.getCode(), successStatus.getMessage(), data);
    }

    public static <T> ApiResponseDto<T> error(ErrorStatus errorStatus) {
        return new ApiResponseDto<>(errorStatus.getCode(), errorStatus.getMessage());
    }
}
