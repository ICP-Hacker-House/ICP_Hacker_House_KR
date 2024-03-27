package io.blockwavelabs.tree.domain.wallet;

import io.blockwavelabs.tree.domain.wallet.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletRepository extends JpaRepository<Wallet, String> {
    boolean existsByWalletAddress(String walletAddress);
    Wallet findByWalletAddress(String walletAddress);
    void deleteByWalletAddress(String walletAddress);
}
