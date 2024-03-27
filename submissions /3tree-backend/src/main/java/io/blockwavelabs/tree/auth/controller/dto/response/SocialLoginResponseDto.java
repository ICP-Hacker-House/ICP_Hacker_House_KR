package io.blockwavelabs.tree.auth.controller.dto.response;

import lombok.*;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SocialLoginResponseDto {
    private Boolean isSignUp;
    private JwtTokenDto jwtTokenDto;

    public static SocialLoginResponseDto of(Boolean isSignUp, JwtTokenDto jwtTokenDto) {
        return new SocialLoginResponseDto(isSignUp, jwtTokenDto);
    }
}
