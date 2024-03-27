package io.blockwavelabs.tree.dto.link.response;

import io.blockwavelabs.tree.domain.link.Link;
import lombok.*;

public class LinkResponseDto {

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class LinkInfoDto {
        private Long id;
        private String linkTitle;
        private String linkUrl;

        public static LinkInfoDto of(Link link){
            return LinkInfoDto.builder()
                    .id(link.getId()).linkTitle(link.getLinkTitle())
                    .linkUrl(link.getLinkUrl())
                    .build();
        }
    }
}
