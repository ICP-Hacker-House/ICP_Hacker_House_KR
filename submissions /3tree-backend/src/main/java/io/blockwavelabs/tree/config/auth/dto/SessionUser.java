package io.blockwavelabs.tree.config.auth.dto;

import io.blockwavelabs.tree.auth.domain.User;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class SessionUser implements Serializable {
    private String name;
    private String email;
    private String picture;

    public SessionUser(User user) {
        this.name = user.getProfileName();
        this.email = user.getSocialId();
        this.picture = user.getProfileImg();
    }
}
