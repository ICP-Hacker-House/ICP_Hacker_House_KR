package io.blockwavelabs.tree.dto.link.legacy;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.blockwavelabs.tree.domain.link.Link;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LinkInfoDto {
    private Long id;
    @JsonProperty(value = "link_title")
    private String linkTitle;
    @JsonProperty(value = "link_url")
    private String linkUrl;

    @Builder
    public LinkInfoDto(Long id, String linkTitle, String linkUrl) {
        this.id = id;
        this.linkTitle = linkTitle;
        this.linkUrl = linkUrl;
    }

    public static LinkInfoDto of(Link link){
        return LinkInfoDto.builder()
                .id(link.getId()).linkTitle(link.getLinkTitle())
                .linkUrl(link.getLinkUrl())
                .build();
    }
}
