package io.blockwavelabs.tree.exception.handler;

import feign.FeignException;
import io.blockwavelabs.tree.common.dto.ApiResponseDto;
import io.blockwavelabs.tree.exception.ErrorStatus;
import io.blockwavelabs.tree.exception.SamTreeException;
import io.blockwavelabs.tree.exception.response.ErrorResult;
import io.blockwavelabs.tree.exception.type.InternalServerExceptionType;
import io.blockwavelabs.tree.exception.type.JwtExceptionType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * ExceptionHandler 설명 : ExceptionHandler에서 BizException 하나만 가지고 여러개의 ExceptionType으로 정의하여 예외반환
 */
@Slf4j
@RestControllerAdvice
public class ExceptionHandler {
    /**
     * 정의한 예외타입에 걸리는 경우는 아래의 메소드에서 예외가 던져짐
     * @param e
     * @return
     */
    @org.springframework.web.bind.annotation.ExceptionHandler(SamTreeException.class)
    public ResponseEntity<ErrorResult> samTreeException(SamTreeException e){
        log.error("SamTreeException throw Exception : {}", e.getErrorCode());
        return ErrorResult.toResponseEntity(e.getErrorCode());
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResult> accessDeniedException(AccessDeniedException e){
        log.error("accessDeniedException throw Exception : {}", e.getMessage());
        return ErrorResult.toResponseEntity(JwtExceptionType.BAD_TOKEN);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResult> notResolvedServerException(Exception e){
        log.error("NotResolvedServerException throw Exception : {}", e);
        return ErrorResult.toResponseEntity(InternalServerExceptionType.INTERNAL_SERVER_ERROR);
    }

    /**
     *
     * DTO에서 Validation에 실패할 경우 어떤 필드가 어떤 이유 때문에 validation에 실패하였는지 error message에 담아서 리턴한다.
     **/
    @org.springframework.web.bind.annotation.ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResult> dtoValidationException(MethodArgumentNotValidException e){
        BindingResult bindingResult = e.getBindingResult();
        StringBuilder builder = new StringBuilder();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            builder.append("[")
                    .append(fieldError.getField())
                    .append("](은)는 ")
                    .append(fieldError.getDefaultMessage())
                    .append(" 입력된 값: [")
                    .append(fieldError.getRejectedValue())
                    .append("]");
        }
        return ErrorResult.toResponseEntity(HttpStatus.BAD_REQUEST,e.getMessage(),"DTO_VALIDATION_ERROR",builder.toString());
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @org.springframework.web.bind.annotation.ExceptionHandler(FeignException.Unauthorized.class)
    public ApiResponseDto feignClientException(FeignException.Unauthorized e) {
        return ApiResponseDto.error(ErrorStatus.TWITTER_TOKEN_INVALID);
    }
}
