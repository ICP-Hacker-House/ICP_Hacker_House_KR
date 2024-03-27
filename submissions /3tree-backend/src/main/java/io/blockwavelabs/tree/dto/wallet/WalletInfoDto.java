package io.blockwavelabs.tree.dto.wallet;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.blockwavelabs.tree.domain.userWallet.UserWallet;
import io.blockwavelabs.tree.domain.wallet.Wallet;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WalletInfoDto {
    private Long id;

    private String walletAddress;
    private String walletType;

    @Builder
    public WalletInfoDto(Long id, String walletAddress, String walletType) {
        this.id = id;
        this.walletAddress = walletAddress;
        this.walletType = walletType;
    }

    public static WalletInfoDto of(Wallet wallet){
        return WalletInfoDto.builder()
                .id(wallet.getId())
                .walletAddress(wallet.getWalletAddress())
                .walletType(wallet.getWalletType().name())
                .build();
    }
}
