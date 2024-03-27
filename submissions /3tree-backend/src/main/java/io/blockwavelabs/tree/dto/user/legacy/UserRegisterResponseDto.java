package io.blockwavelabs.tree.dto.user.legacy;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserRegisterResponseDto {
    private Long id;
    private String socialId;
    private String authToken;

    @Builder
    public UserRegisterResponseDto(Long id, String socialId/*, String authToken*/) {
        this.id = id;
        this.socialId = socialId;
        //this.authToken = authToken;
    }
}
