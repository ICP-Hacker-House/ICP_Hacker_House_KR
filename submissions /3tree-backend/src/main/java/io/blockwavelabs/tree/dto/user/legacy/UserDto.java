package io.blockwavelabs.tree.dto.user.legacy;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

public class UserDto implements Serializable{
    @Data
    @Builder
    public static class socialLoginResponse {
        private final String status;
        private final String name;
        private final String email;
        private final String img;

        public static socialLoginResponse response(String name, String email, String img, String status) {
            return socialLoginResponse.builder()
                    .status(status)
                    .name(name)
                    .email(email)
                    .img(img)
                    .build();
        }
    }
}
