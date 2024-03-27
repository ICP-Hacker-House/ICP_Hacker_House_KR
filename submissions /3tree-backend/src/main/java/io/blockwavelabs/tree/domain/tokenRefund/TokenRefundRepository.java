package io.blockwavelabs.tree.domain.tokenRefund;

import io.blockwavelabs.tree.domain.tokenSendTrx.TokenSendTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public interface TokenRefundRepository extends JpaRepository<TokenRefund, Long> {
    Optional<TokenRefund> findByTokenSendTransaction(TokenSendTransaction tokenSendTransaction);

    //@Modifying(clearAutomatically = true)
    @Query(value = "SELECT tr FROM TokenRefund tr WHERE (tr.refundStatus='UNSTARTED' OR tr.refundStatus='FAILED') AND tr.tokenUdenom=:tokenUdenom")
    List<TokenRefund> getRefundTrxCandidates(@Param("tokenUdenom") String tokenUdenom);

    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE token_refund SET refund_status= :refundStatus, refund_at = NOW() WHERE `index`=:index", nativeQuery = true)
    void changeRefundStatus(@Param("index") Long index, @Param("refundStatus") String refundStatus);

    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE token_refund  SET transaction_fee=:transactionFee, gas_price=:gasPrice, gas_limit=:gasLimit, gas_used_by_txn=:gasUsedByTxn WHERE `index`=:index", nativeQuery = true)
    void saveTokenRefundGasFee(@Param("index") Long index, @Param("transactionFee") Float transactionFee, @Param("gasPrice") Float gasPrice
            , @Param("gasLimit") Float gasLimit, @Param("gasUsedByTxn") Float gasUsedByTxn);

    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE token_refund SET refund_transaction_hash=:refundTrxHash WHERE `index`=:index", nativeQuery = true)
    void saveTokenRefundHash(@Param("index") Long index, @Param("refundTrxHash") String refundTrxHash);
}
