package io.blockwavelabs.tree.dto.token;


import io.blockwavelabs.tree.auth.domain.SocialPlatformType;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SendTrxStatusToggleReqDto {
    private Long sendTrxIndex;
    private Boolean isValid;

    private SocialPlatformType socialPlatformType;

}
