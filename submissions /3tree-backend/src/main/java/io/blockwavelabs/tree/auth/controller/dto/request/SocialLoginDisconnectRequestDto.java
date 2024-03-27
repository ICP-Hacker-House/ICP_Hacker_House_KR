package io.blockwavelabs.tree.auth.controller.dto.request;

import io.blockwavelabs.tree.auth.domain.SocialPlatformType;
import lombok.*;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SocialLoginDisconnectRequestDto {

    private SocialPlatformType socialPlatformType;
}
