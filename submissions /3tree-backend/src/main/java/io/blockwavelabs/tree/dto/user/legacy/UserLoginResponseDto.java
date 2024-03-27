package io.blockwavelabs.tree.dto.user.legacy;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserLoginResponseDto {
    private TokenDto tokenDto;
    private UserDto.socialLoginResponse socialLoginResponse;

    @Builder
    public UserLoginResponseDto(TokenDto tokenDto, UserDto.socialLoginResponse socialLoginResponse) {
        this.tokenDto = tokenDto;
        this.socialLoginResponse = socialLoginResponse;
    }
}
