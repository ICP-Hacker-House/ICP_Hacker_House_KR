package io.blockwavelabs.tree.dto.user.legacy;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserInfoDto {
    @JsonProperty(value = "profile_name")
    private String profileName;
    @JsonProperty(value = "profile_img")
    private String profileImg;

    @JsonProperty(value = "profile_bio")
    private String profileBio;

    @JsonProperty(value = "user_id")
    private String userId;

    @JsonProperty(value = "social_id")
    private String socialId;

    @Builder
    public UserInfoDto(String profileName, String profileImg, String profileBio, String userId, String socialId) {
        this.profileName = profileName;
        this.profileImg = profileImg;
        this.profileBio = profileBio;
        this.userId = userId;
        this.socialId = socialId;
    }
}
