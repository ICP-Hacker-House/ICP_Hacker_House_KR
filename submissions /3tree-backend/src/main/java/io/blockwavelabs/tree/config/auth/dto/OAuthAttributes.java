package io.blockwavelabs.tree.config.auth.dto;

import io.blockwavelabs.tree.auth.domain.Role;
import io.blockwavelabs.tree.auth.domain.SocialPlatformType;
import io.blockwavelabs.tree.auth.domain.User;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
public class OAuthAttributes {
    private Map<String,Object> attributes;
    private String nameAttributeKey;
    private String name;
    private String email;
    private String picture;
    private SocialPlatformType socialPlatform;
    private String userId;

    @Builder
    public OAuthAttributes(Map<String,Object> attributes, String nameAttributeKey, String name, String email, String picture, SocialPlatformType socialPlaform, String userId){
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.name = name;
        this.email = email;
        this.picture = picture;
        this.socialPlatform = socialPlaform;
        this.userId = userId;
    }

    public static OAuthAttributes of(String registrationId,
                                     String userNameAttributeName,
                                     Map<String, Object> attributes) {
        return ofGoogle(userNameAttributeName, attributes);
    }

    private static OAuthAttributes ofGoogle(String userNameAttributeName,
                                            Map<String, Object> attributes) {
        // 1. social platform 추가
        SocialPlatformType socialPlatform = SocialPlatformType.GOOGLE;
        // 2. user ID 랜덤 생성 추가
        String userId = createDefaultUserIdWithEmail((String) attributes.get("email"));

        return OAuthAttributes.builder()
                .name((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .picture((String) attributes.get("picture"))
                .socialPlaform(socialPlatform)
                .userId(userId)
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    public User toEntity(){
        return User.builder()
                .socialId(email)
                .profileName(name)
                .profileImg(picture)
                .socialPlatform(socialPlatform)
                .userId(name)
                .role(Role.USER)
                .build();
    }

    public static String createDefaultUserIdWithEmail(String email){
        String[] split1_ = email.split("@");
        String[] split2_ = split1_[1].split("\\.");
        String id = split1_[0];
        String platform = split2_[0];

        return id + "_" + platform;
    }
}
