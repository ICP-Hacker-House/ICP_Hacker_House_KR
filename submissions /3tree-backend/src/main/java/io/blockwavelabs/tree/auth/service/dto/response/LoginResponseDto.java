package io.blockwavelabs.tree.auth.service.dto.response;

import lombok.*;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class LoginResponseDto {

    private String userId;

    public static LoginResponseDto of(String userId) {
        return new LoginResponseDto(userId);
    }
}
