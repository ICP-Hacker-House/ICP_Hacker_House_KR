package io.blockwavelabs.tree.dto.profile.response;

import io.blockwavelabs.tree.auth.domain.SocialPlatformType;
import lombok.*;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SocialAccountResponseDto {

    private SocialPlatformType socialPlatformType;
    private String socialId;

    public static SocialAccountResponseDto of(SocialPlatformType socialPlatformType, String socialId) {
        return new SocialAccountResponseDto(socialPlatformType, socialId);
    }
}
