package io.blockwavelabs.tree.dto.user.legacy;

import io.blockwavelabs.tree.domain.link.Link;
import io.blockwavelabs.tree.domain.profileDecorate.ProfileDecorate;
import io.blockwavelabs.tree.auth.domain.Role;
import io.blockwavelabs.tree.auth.domain.SocialPlatformType;
import io.blockwavelabs.tree.auth.domain.User;
import io.blockwavelabs.tree.dto.wallet.WalletInfoDto;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class UserTotalInfo {
    private totalInfo user;
    private ProfileDecorate profileDecorate;
    private List<Link> links;
    private List<WalletInfoDto> wallets;

    @Data
    public static class totalInfo{
        private Long index;
        private String profileName;
        private String profileImg;
        private String profileBio;
        private String userId;
        private String socialId;
        private Role role;
        private SocialPlatformType socialPlatform;

        @Builder
        public totalInfo(User user) {
            this.index = user.getId();
            this.profileName = user.getProfileName();
            this.profileImg = user.getProfileImg();
            this.profileBio = user.getProfileBio();
            this.userId = user.getUserId();
            this.socialId = user.getSocialId();
            this.role = user.getRole();
            this.socialPlatform = user.getSocialPlatform();
        }
    }

    @Builder
    public UserTotalInfo(totalInfo user, ProfileDecorate profileDecorate, List<Link> links, List<WalletInfoDto> wallets) {
        this.user = user;
        this.profileDecorate = profileDecorate;
        this.links = links;
        this.wallets = wallets;
    }
}
