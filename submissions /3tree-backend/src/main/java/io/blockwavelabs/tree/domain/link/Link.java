package io.blockwavelabs.tree.domain.link;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.blockwavelabs.tree.auth.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@DynamicUpdate
@NoArgsConstructor
public class Link {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    @NotNull
    private String linkTitle;

    @NotNull
    private String linkUrl;

    @Builder
    public Link(User user, String linkTitle, String linkUrl){
        this.user = user;
        this.linkTitle = linkTitle;
        this.linkUrl = linkUrl;
    }
}
