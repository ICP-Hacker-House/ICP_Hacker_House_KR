package io.blockwavelabs.tree.dto.profile.legacy;

import com.google.gson.annotations.SerializedName;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProfileDecorateEditReqDto {

    @SerializedName(value = "background_color")
    private String backgroundColor;

    @SerializedName(value = "button_color")
    private String buttonColor;

    @SerializedName(value = "button_font_color")
    private String buttonFontColor;

    @SerializedName(value = "font_color")
    private String fontColor;

    // SerializedName 레퍼런스 : https://blog.hodory.dev/2019/06/04/json-property-not-working/
}
