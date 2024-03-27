package io.blockwavelabs.tree.dto.profile.request;

import com.google.gson.annotations.SerializedName;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ProfileRequestDto {

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class ProfileDecorateEditDto {
        // SerializedName 레퍼런스 : https://blog.hodory.dev/2019/06/04/json-property-not-working/
        @SerializedName(value = "background_color")
        private String backgroundColor;

        @SerializedName(value = "button_color")
        private String buttonColor;

        @SerializedName(value = "button_font_color")
        private String buttonFontColor;

        @SerializedName(value = "font_color")
        private String fontColor;
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public class ProfileEditDto {
        @SerializedName(value = "profile_name")
        private String profileName;
        @SerializedName(value = "profile_description")
        private String profileDescription;
    }
}
