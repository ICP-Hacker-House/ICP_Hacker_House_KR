package io.blockwavelabs.tree.dto.wallet;

import io.blockwavelabs.tree.domain.userWallet.UserWallet;
import io.blockwavelabs.tree.domain.wallet.Wallet;
import io.blockwavelabs.tree.domain.wallet.WalletTypeEnum;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserWalletInfoDto {
    private Long index;
    private Long userIndex;
    private String walletAddress;
    private WalletTypeEnum walletType;

    @Builder
    public UserWalletInfoDto(Long index, Long userIndex, String walletAddress, WalletTypeEnum walletType){
        this.index = index;
        this.userIndex = userIndex;
        this.walletAddress = walletAddress;
        this.walletType = walletType;
    }

    public static UserWalletInfoDto of(UserWallet userWallet){
        return UserWalletInfoDto.builder()
                .index(userWallet.getId())
                .userIndex(userWallet.getUser().getId())
                .walletAddress(userWallet.getWallet().getWalletAddress())
                .walletType(userWallet.getWalletType())
                .build();
    }
}
