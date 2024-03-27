package io.blockwavelabs.tree.dto.token;

import io.blockwavelabs.tree.auth.domain.SocialPlatformType;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class CreateSendTokenTrxDto {
    private String senderWalletAddress;
    private String senderTokenWalletType;
    private SocialPlatformType receiverSocialPlatform;
    private String receiverSocialId;
    private String tokenUdenom;
    private Float tokenAmount;
    private String transactionHash;
    private Integer transactionEscrowId;
    private LocalDateTime expiredAt;
    private String tokenContractAddress;
    private String networkId;
}
