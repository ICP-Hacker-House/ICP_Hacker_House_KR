package io.blockwavelabs.tree.domain.tokenRefund;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.blockwavelabs.tree.domain.tokenSendTrx.TokenSendTransaction;
import io.blockwavelabs.tree.domain.wallet.WalletTypeEnum;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Getter
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class TokenRefund {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name="transaction_id")
    private TokenSendTransaction tokenSendTransaction;

    private String senderUserId;

    @NotNull
    private String senderWalletAddress;

    @Enumerated(EnumType.STRING)
    @NotNull
    private WalletTypeEnum senderTokenWalletType;

    @NotNull
    private String tokenUdenom;

    @NotNull
    private Float tokenAmount;

    private String networkId;

    private LocalDateTime expiredAt;

    @Enumerated(EnumType.STRING)
    @ColumnDefault("UNSTARTED")
    private RefundStatusEnum refundStatus;

    private LocalDateTime refundAt;

    @LastModifiedDate
    private LocalDateTime updateAt;

    private String refundTransactionHash;
    private Float transactionFee;
    private Float gasPrice;
    private Float gasLimit;
    private Float gasUsedByTxn;

    @Builder
    public TokenRefund(TokenSendTransaction tokenSendTransaction, String senderWalletAddress, WalletTypeEnum senderTokenWalletType, String tokenUdenom, Float tokenAmount){
        this.tokenSendTransaction = tokenSendTransaction;
        this.senderWalletAddress = senderWalletAddress;
        this.senderTokenWalletType = senderTokenWalletType;
        this.tokenUdenom = tokenUdenom;
        this.tokenAmount = tokenAmount;
    }
}
