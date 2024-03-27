package io.blockwavelabs.tree.dto.twitter;

import lombok.*;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TweetResponseDto {

    private String tweetLink;

    public static TweetResponseDto of(String tweetLink) {
        return new TweetResponseDto(tweetLink);
    }
}
