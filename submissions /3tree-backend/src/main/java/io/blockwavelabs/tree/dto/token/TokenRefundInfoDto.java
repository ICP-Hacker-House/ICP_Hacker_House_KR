package io.blockwavelabs.tree.dto.token;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.blockwavelabs.tree.domain.tokenRefund.RefundStatusEnum;
import io.blockwavelabs.tree.domain.tokenRefund.TokenRefund;
import io.blockwavelabs.tree.domain.tokenSendTrx.TokenSendTransaction;
import io.blockwavelabs.tree.domain.wallet.WalletTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenRefundInfoDto {
        private Long id;
        private Long tokenSendTransactionIndex;
        private String senderUserId;
        private String senderWalletAddress;
        private String senderTokenWalletType;
        private String tokenUdenom;
        private Float tokenAmount;
        private String networkId;
        private LocalDateTime expiredAt;
        private String refundStatus;
        private LocalDateTime refundAt;
        private LocalDateTime updateAt;
        private String refundTransactionHash;
        private Float transactionFee;
        private Float gasPrice;
        private Float gasLimit;
        private Float gasUsedByTxn;

        public static TokenRefundInfoDto of(TokenRefund tokenRefund){
            return TokenRefundInfoDto.builder()
                    .id(tokenRefund.getId())
                    .tokenSendTransactionIndex(tokenRefund.getTokenSendTransaction().getId())
                    .senderUserId(tokenRefund.getSenderUserId())
                    .senderWalletAddress(tokenRefund.getSenderWalletAddress())
                    .senderTokenWalletType(tokenRefund.getSenderTokenWalletType().name())
                    .tokenUdenom(tokenRefund.getTokenUdenom())
                    .tokenAmount(tokenRefund.getTokenAmount())
                    .networkId(tokenRefund.getNetworkId())
                    .expiredAt(tokenRefund.getExpiredAt())
                    .refundStatus(tokenRefund.getRefundStatus().toString())
                    .refundAt(tokenRefund.getRefundAt())
                    .updateAt(tokenRefund.getUpdateAt())
                    .refundTransactionHash(tokenRefund.getRefundTransactionHash())
                    .transactionFee(tokenRefund.getTransactionFee())
                    .gasPrice(tokenRefund.getGasPrice())
                    .gasLimit(tokenRefund.getGasLimit())
                    .gasUsedByTxn(tokenRefund.getGasUsedByTxn())
                    .build();
        }

}
