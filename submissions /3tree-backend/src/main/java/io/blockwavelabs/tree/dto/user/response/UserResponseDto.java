package io.blockwavelabs.tree.dto.user.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.blockwavelabs.tree.domain.link.Link;
import io.blockwavelabs.tree.domain.profileDecorate.ProfileDecorate;
import io.blockwavelabs.tree.auth.domain.LanguageEnum;
import io.blockwavelabs.tree.auth.domain.Role;
import io.blockwavelabs.tree.auth.domain.SocialPlatformType;
import io.blockwavelabs.tree.auth.domain.User;
import io.blockwavelabs.tree.dto.wallet.WalletInfoDto;
import lombok.*;

import java.util.List;

public class UserResponseDto {

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class TokenDto {
        @JsonProperty(value = "grant_type")
        private String grantType;

        @JsonProperty(value = "access_token")
        private String accessToken;

        @JsonProperty(value = "refresh_token")
        private String refreshToken;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class SocialLoginResponse {
        private String status;
        private String name;
        private String email;
        private String img;
        private String userId;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class UserLoginResponseDto {
        private TokenDto tokenDto;
        private SocialLoginResponse socialLoginResponse;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class UserTotalInfo {
        private TotalInfo user;
        private ProfileDecorate profileDecorate;
        private List<Link> links;
        private List<WalletInfoDto> wallets;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class TotalInfo {
        private Long index;
        private String profileName;
        private String profileImg;
        private String profileBio;
        private String userId;
        private String socialId;
        private Role role;
        private SocialPlatformType socialPlatform;
        private LanguageEnum language;


        public static TotalInfo of(User user) {
            return TotalInfo.builder()
                    .index(user.getId())
                    .profileName(user.getProfileName())
                    .profileImg(user.getProfileImg())
                    .profileBio(user.getProfileBio())
                    .userId(user.getUserId())
                    .socialId(user.getSocialId())
                    .role(user.getRole())
                    .socialPlatform(user.getSocialPlatform())
                    .language(user.getLanguage())
                    .build();
        }
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class TotalInfoIncludeDid {
        private Long index;
        private String profileName;
        private String profileImg;
        private String profileBio;
        private String userId;
        private String socialId;
        private Role role;
        private SocialPlatformType socialPlatform;
        private LanguageEnum language;
        private String did;
        private String frontKey;


        public static TotalInfoIncludeDid of(User user) {
            return TotalInfoIncludeDid.builder()
                    .index(user.getId())
                    .profileName(user.getProfileName())
                    .profileImg(user.getProfileImg())
                    .profileBio(user.getProfileBio())
                    .userId(user.getUserId())
                    .socialId(user.getSocialId())
                    .role(user.getRole())
                    .socialPlatform(user.getSocialPlatform())
                    .language(user.getLanguage())
                    .did(user.getDid())
                    .frontKey(user.getFrontKey())
                    .build();
        }
    }

}
