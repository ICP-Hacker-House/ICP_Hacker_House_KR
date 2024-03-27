package io.blockwavelabs.tree.dto.link.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class LinkRequestDto {
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class LinkAddDto {
        @JsonProperty(value = "link_title")
        private String linkTitle;
        @JsonProperty(value = "link_url")
        private String linkUrl;
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class LinkEditDto {
        private Long id;
        private String title;
        private String url;
    }
}
