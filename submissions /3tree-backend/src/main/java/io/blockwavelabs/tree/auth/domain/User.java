package io.blockwavelabs.tree.auth.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.blockwavelabs.tree.common.domain.AuditingTimeEntity;
import io.blockwavelabs.tree.domain.link.Link;
import io.blockwavelabs.tree.domain.tokenSendTrx.TokenSendTransaction;
import io.blockwavelabs.tree.domain.userWallet.UserWallet;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@DynamicInsert
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends AuditingTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String profileName;

    private String profileImg;

    private String profileBio;

    @Column(name="user_id", unique = true)
    private String userId;

    @Column(unique = true)
    private String socialId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Enumerated(EnumType.STRING)
    private SocialPlatformType socialPlatform;

    @Enumerated(EnumType.STRING)
    @ColumnDefault("ENG")
    private LanguageEnum language;

    @Column(unique = true)
    private String did;

    @Column(unique = true)
    private String frontKey;

    @JsonManagedReference
    @OneToMany(mappedBy="user")
    private List<Link> links = new ArrayList<>();

    @JsonManagedReference
    @OneToMany(mappedBy = "user")
    private List<UserWallet> userWallets = new ArrayList<>();

    @JsonManagedReference
    @OneToMany(mappedBy = "senderUser")
    private List<TokenSendTransaction> sendTrxs = new ArrayList<>();

    @Builder
    public User(String userId, SocialPlatformType socialPlatform, String socialId, String profileImg, String profileName, Role role, String profileBio){
        this.userId = userId;
        this.socialPlatform = socialPlatform;
        this.socialId = socialId;
        this.profileImg = profileImg;
        this.profileName = profileName;
        this.role = role;
        this.profileBio = profileBio;
    }

    public User update(String profileName, String profileImg) {
        this.profileName = profileName;
        this.profileImg = profileImg;

        return this;
    }

    public String getRoleKey(){
        return this.role.getKey();
    }
}
