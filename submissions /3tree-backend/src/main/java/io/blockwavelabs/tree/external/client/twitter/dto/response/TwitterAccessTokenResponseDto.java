package io.blockwavelabs.tree.external.client.twitter.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TwitterAccessTokenResponseDto {

    private String accessToken;
    private String refreshToken;

    public static TwitterAccessTokenResponseDto of(String accessToken, String refreshToken) {
        return new TwitterAccessTokenResponseDto(accessToken, refreshToken);
    }
}
