package io.blockwavelabs.tree.exception;

import io.blockwavelabs.tree.exception.type.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class SamTreeException extends RuntimeException{
    private final ErrorCode errorCode;
}
