package io.blockwavelabs.tree.auth.controller.dto.response;

import lombok.*;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class JwtTokenDto {

    private String accessToken;
    private String refreshToken;
}
