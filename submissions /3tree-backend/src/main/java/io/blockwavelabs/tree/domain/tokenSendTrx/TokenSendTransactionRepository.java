package io.blockwavelabs.tree.domain.tokenSendTrx;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TokenSendTransactionRepository extends JpaRepository<TokenSendTransaction, Long> {
    Optional<TokenSendTransaction> findTokenSendTransactionById(Long id);
    Optional<TokenSendTransaction> findByLinkKey(String linkKey);
    boolean existsByLinkKey(String linkKey);

    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE token_send_transaction SET is_valid = :valid WHERE `id` = :index", nativeQuery = true)
    int updateValid( @Param(value="index") Long index, @Param(value="valid") boolean valid);

    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE token_send_transaction SET receiver_wallet_address=:receiver_wallet_address, receiver_token_wallet_type=:receiver_token_wallet_type, transaction_gas_fee=:transaction_gas_fee, receiver_user_index = :receiver_user_index, receiver_user_id = :receiver_user_id, is_valid=false WHERE `id`=:index", nativeQuery = true)
    int updateAfterReceive( @Param(value="index") Long index, @Param(value="receiver_wallet_address") String receiver_wallet_address, @Param(value="receiver_token_wallet_type") String receiver_token_wallet_type, @Param(value="transaction_gas_fee") Float transaction_gas_fee, @Param(value = "receiver_user_index") Long receiver_user_index, @Param(value = "receiver_user_id") String receiver_user_id);

    @Query(value = "UPDATE token_send_transaction SET is_expired = True WHERE is_expired = False AND expired_at <= CURRENT_TIME;", nativeQuery = true)
    void updateExpired();
}