package io.blockwavelabs.tree.dto.profile.legacy;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProfileEditDto {
    @JsonProperty(value = "profile_name")
    private String profileName;

    @JsonProperty(value = "profile_description")
    private String profileDescription;
}
