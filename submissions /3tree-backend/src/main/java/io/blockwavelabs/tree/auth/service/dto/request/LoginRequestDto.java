package io.blockwavelabs.tree.auth.service.dto.request;

import lombok.*;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class LoginRequestDto {

    private String code;

    public static LoginRequestDto of(String code) {
        return new LoginRequestDto(code);
    }
}
