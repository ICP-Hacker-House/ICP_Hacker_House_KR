package io.blockwavelabs.tree.domain.tokenSendTrx;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.blockwavelabs.tree.auth.domain.SocialUser;
import io.blockwavelabs.tree.common.domain.AuditingTimeEntity;
import io.blockwavelabs.tree.domain.tokenRefund.TokenRefund;
import io.blockwavelabs.tree.auth.domain.SocialPlatformType;
import io.blockwavelabs.tree.auth.domain.User;
import io.blockwavelabs.tree.domain.wallet.WalletTypeEnum;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Getter
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor
public class TokenSendTransaction extends AuditingTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonBackReference
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="user_id")
    private SocialUser senderUser;

    @Column(name="sender_user_id")
    private String senderUserId;

    @NotNull
    private String senderWalletAddress;

    @Enumerated(EnumType.STRING)
    @NotNull
    private WalletTypeEnum senderTokenWalletType;

    @Enumerated(EnumType.STRING)
    @NotNull
    private SocialPlatformType receiverSocialPlatform;

    @NotNull
    private String receiverSocialId;

    private String receiverWalletAddress;

    private String receiverTokenWalletType;

    private String tokenUdenom;
    private Float tokenAmount;

    @Column(unique=true)
    private String linkKey;

    @ColumnDefault("false")
    private Boolean isExpired;

    @ColumnDefault("true")
    private Boolean isValid;

    private Float transactionGasFee;
    private String transactionHash;
    private String tokenContractAddress;
    private String networkId;
    private Integer transactionEscrowId;

    private LocalDateTime expiredAt;

    private Long receiverUserIndex;

    private String receiverUserId;

    @OneToOne(mappedBy = "tokenSendTransaction")
    private TokenRefund tokenRefund;

//    @Builder
//    public TokenSendTransaction(User senderUser, String senderWalletAddress, WalletTypeEnum senderTokenWalletType, SocialPlatformEnum receiverSocialPlatform, String receiverSocialId){
//        this.senderUser = senderUser;
//        this.senderWalletAddress = senderWalletAddress;
//        this.senderTokenWalletType = senderTokenWalletType;
//        this.receiverSocialPlatform = receiverSocialPlatform;
//        this.receiverSocialId = receiverSocialId;
//    }

    @Builder
    public TokenSendTransaction(SocialUser senderUser, String senderUserId, String senderWalletAddress, WalletTypeEnum senderTokenWalletType, SocialPlatformType receiverSocialPlatform, String receiverSocialId, String tokenUdenom, Float tokenAmount, String transactionHash, Integer transactionEscrowId, LocalDateTime expiredAt, String tokenContractAddress, String networkId, String linkKey){
        this.senderUser = senderUser;
        this.senderUserId = senderUserId;
        this.senderWalletAddress = senderWalletAddress;
        this.senderTokenWalletType = senderTokenWalletType;
        this.receiverSocialPlatform = receiverSocialPlatform;
        this.receiverSocialId = receiverSocialId;
        this.tokenUdenom = tokenUdenom;
        this.tokenAmount = tokenAmount;
        this.transactionHash = transactionHash;
        this.transactionEscrowId = transactionEscrowId;
        this.expiredAt = expiredAt;
        this.tokenContractAddress = tokenContractAddress;
        this.networkId = networkId;
        this.linkKey = linkKey;
    }
}
