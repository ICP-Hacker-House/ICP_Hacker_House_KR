package io.blockwavelabs.tree.auth.domain;

import io.blockwavelabs.tree.common.domain.AuditingTimeEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

@Entity
@Getter
@DynamicInsert
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SocialUser extends AuditingTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private String socialId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private SocialPlatformType socialPlatformType;

    @Column(nullable = false)
    private String accessToken;

    @Column(nullable = false)
    private String refreshToken;

    @Column(nullable = false)
    @ColumnDefault("true")
    private Boolean isConnected;

    private SocialUser(User user, String socialId, String name, String username, String accessToken, String refreshToken, SocialPlatformType socialPlatformType) {
        this.user = user;
        this.socialId = socialId;
        this.name = name;
        this.username = username;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.socialPlatformType = socialPlatformType;
    }

    public static SocialUser newInstance(User user, String socialId, String name, String username, String accessToken, String refreshToken, SocialPlatformType socialPlatformType) {
        return new SocialUser(user, socialId, name, username, accessToken, refreshToken, socialPlatformType);
    }

    public void updateAccountState(boolean isConnected, String accessToken, String refreshToken) {
        this.isConnected = isConnected;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
