package io.blockwavelabs.tree.exception.type;

import org.springframework.http.HttpStatus;

public interface ErrorCode {
    String getErrorCode();
    String getMessage();
    HttpStatus getHttpStatus();
}
