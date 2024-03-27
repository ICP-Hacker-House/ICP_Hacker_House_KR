package io.blockwavelabs.tree.external.client.twitter.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TwitterUserProfileResponseDto {

    private TwitterUserProfileData data;
}
