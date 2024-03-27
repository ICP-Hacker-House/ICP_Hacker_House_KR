package io.blockwavelabs.tree.dto.twitter;

import lombok.*;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PostTweetRequest {

    private String receivedTime;
}
