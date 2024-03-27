package io.blockwavelabs.tree.dto.token;

import io.blockwavelabs.tree.domain.tokenSendTrx.TokenSendTransaction;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenSendTrxInfoDto {
    private Long id;
    private Long senderUser;
    private String senderUserId;
    private String senderWalletAddress;
    private String senderTokenWalletType;
    private String receiverSocialPlatform;
    private String receiverSocialId;
    private String receiverWalletAddress;
    private String receiverTokenWalletType;
    private String tokenUdenom;
    private Float tokenAmount;
    private String linkKey;
    private Boolean isExpired;
    private Boolean isValid;
    private Float transactionGasFee;
    private String transactionHash;
    private String tokenContractAddress;
    private String networkId;
    private Integer transactionEscrowId;
    private LocalDateTime createdAt;
    private LocalDateTime expiredAt;
    private Long receiverUserIndex;
    private String receiverUserId;

//    @Builder
//    public TokenSendTrxInfoDto(Long id, Long senderUser, String senderUserId, String senderWalletAddress, String senderTokenWalletType, String receiverSocialPlatform, String receiverSocialId, String receiverWalletAddress, String receiverTokenWalletType, String tokenUdenom, Float tokenAmount, String linkKey, Boolean isExpired, Boolean isValid, Float transactionGasFee, String transactionHash, String tokenContractAddress, String networkId, Integer transactionEscrowId, LocalDateTime createdAt, LocalDateTime expiredAt, Long receiverUserIndex, String receiverUserId){
//        this.id = id;
//        this.senderUser = senderUser;
//        this.senderUserId = senderUserId;
//        this.senderWalletAddress = senderWalletAddress;
//        this.senderTokenWalletType = senderTokenWalletType;
//        this.receiverSocialPlatform = receiverSocialPlatform;
//        this.receiverSocialId = receiverSocialId;
//        this.receiverWalletAddress = receiverWalletAddress;
//        this.receiverTokenWalletType = receiverTokenWalletType;
//        this.tokenUdenom = tokenUdenom;
//        this.tokenAmount = tokenAmount;
//        this.linkKey = linkKey;
//        this.isExpired = isExpired;
//        this.isValid = isValid;
//        this.transactionGasFee = transactionGasFee;
//        this.transactionHash = transactionHash;
//        this.tokenContractAddress = tokenContractAddress;
//        this.networkId = networkId;
//        this.transactionEscrowId = transactionEscrowId;
//        this.createdAt = createdAt;
//        this.expiredAt = expiredAt;
//        this.receiverUserIndex = receiverUserIndex;
//        this.receiverUserId = receiverUserId;
//    }

    public static TokenSendTrxInfoDto of(TokenSendTransaction tokenSendTrx){
        return TokenSendTrxInfoDto.builder()
                .id(tokenSendTrx.getId())
                .senderUser(tokenSendTrx.getSenderUser().getId())
                .senderUserId(tokenSendTrx.getSenderUserId())
                .senderWalletAddress(tokenSendTrx.getSenderWalletAddress())
                .senderTokenWalletType(tokenSendTrx.getSenderTokenWalletType().name())
                .receiverSocialPlatform(tokenSendTrx.getReceiverSocialPlatform().name())
                .receiverSocialId(tokenSendTrx.getReceiverSocialId())
                .receiverWalletAddress(tokenSendTrx.getReceiverWalletAddress())
                .receiverTokenWalletType(tokenSendTrx.getReceiverTokenWalletType())
                .tokenUdenom(tokenSendTrx.getTokenUdenom())
                .tokenAmount(tokenSendTrx.getTokenAmount())
                .linkKey(tokenSendTrx.getLinkKey())
                .isExpired(tokenSendTrx.getIsExpired())
                .isValid(tokenSendTrx.getIsValid())
                .transactionGasFee(tokenSendTrx.getTransactionGasFee())
                .transactionHash(tokenSendTrx.getTransactionHash())
                .tokenContractAddress(tokenSendTrx.getTokenContractAddress())
                .networkId(tokenSendTrx.getNetworkId())
                .transactionEscrowId(tokenSendTrx.getTransactionEscrowId())
                .createdAt(tokenSendTrx.getCreatedAt())
                .expiredAt(tokenSendTrx.getExpiredAt())
                .receiverUserIndex(tokenSendTrx.getReceiverUserIndex())
                .receiverUserId(tokenSendTrx.getReceiverUserId())
                .build();
    }
}


