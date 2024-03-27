package io.blockwavelabs.tree.auth.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum SocialPlatformType {
    GOOGLE("구글"),
    TWITTER("트위터"),
    POLYGONID("폴리곤"),
    ;

    private final String value;
}
