package io.blockwavelabs.tree.dto.user.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

public class UserRequestDto {

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class LoginReqDto {
        @NotBlank
        private String email;

        @Builder
        public LoginReqDto(String email) {
            this.email = email;
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class UserLoginRequestDto {
        private String socialId;

        @Builder
        public UserLoginRequestDto(String socialId) {
            this.socialId = socialId;
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class PolygonDidDto{
        private String did;
        private String frontKey;

        @Builder
        public PolygonDidDto(String did, String frontKey) {
            this.did = did;
            this.frontKey = frontKey;
        }
    }
}
