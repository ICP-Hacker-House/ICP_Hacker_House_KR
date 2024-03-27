package io.blockwavelabs.tree.auth.controller.dto.request;

import io.blockwavelabs.tree.auth.domain.SocialPlatformType;
import io.blockwavelabs.tree.auth.service.dto.request.LoginRequestDto;
import lombok.*;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SocialLoginRequestDto {

    private String code;
    private SocialPlatformType socialPlatformType;

    public LoginRequestDto toServiceDto() {
        return LoginRequestDto.of(code);
    }
}
