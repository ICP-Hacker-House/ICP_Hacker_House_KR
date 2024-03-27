package io.blockwavelabs.tree.domain.userWallet;

import io.blockwavelabs.tree.auth.domain.User;
import io.blockwavelabs.tree.domain.wallet.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserWalletRepository extends JpaRepository<UserWallet, Long> {
    Optional<UserWallet> findUserWalletById(Long id);
    Optional<UserWallet> findByUser(User user);
    Optional<UserWallet> findByWallet(Wallet wallet);
    boolean existsByWalletAndUser(Wallet wallet, User user);
    Optional<List<UserWallet>> findAllByUser_UserId(String userId);

    @Query(value="DELETE FROM user_wallet WHERE `index`=:id", nativeQuery = true)
    void deleteById(@Param(value = "id")Long id);
}
