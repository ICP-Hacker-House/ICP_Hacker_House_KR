package io.blockwavelabs.tree.dto.link.legacy;

import io.blockwavelabs.tree.domain.link.Link;
import io.blockwavelabs.tree.auth.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LinkSaveRequestDto {
    private User user;
    private Integer userIndex;
    private String linkTitle;
    private String linkUrl;

    @Builder
    public LinkSaveRequestDto(User user, String linkTitle, String linkUrl){
        this.user = user;
        this.linkTitle = linkTitle;
        this.linkUrl = linkUrl;
    }

    @Builder
    public LinkSaveRequestDto(Integer userIndex, String linkTitle, String linkUrl){
        this.userIndex = userIndex;
        this.linkTitle = linkTitle;
        this.linkUrl = linkUrl;
    }

    public void setUser(User user){
        this.user = user;
    }

    public Link toEntity(){
        return Link.builder()
                .user(user)
                .linkTitle(linkTitle)
                .linkUrl(linkUrl)
                .build();
    }
}
