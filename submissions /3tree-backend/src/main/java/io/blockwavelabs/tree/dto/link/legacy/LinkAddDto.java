package io.blockwavelabs.tree.dto.link.legacy;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LinkAddDto {
    private String linkTitle;
    private String linkUrl;
}
