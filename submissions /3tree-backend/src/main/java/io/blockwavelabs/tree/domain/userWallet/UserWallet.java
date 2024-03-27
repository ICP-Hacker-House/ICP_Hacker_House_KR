package io.blockwavelabs.tree.domain.userWallet;


import com.fasterxml.jackson.annotation.JsonBackReference;
import io.blockwavelabs.tree.domain.wallet.Wallet;
import io.blockwavelabs.tree.auth.domain.User;
import io.blockwavelabs.tree.domain.wallet.WalletTypeEnum;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@Getter
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor
public class UserWallet {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonBackReference
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    @JsonBackReference
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="wallet_address")
    private Wallet wallet;

    @Enumerated(EnumType.STRING)
    private WalletTypeEnum walletType;

    @Builder
    public UserWallet(User user, Wallet wallet, WalletTypeEnum walletType){
        this.user = user;
        this.wallet = wallet;
        this.walletType = walletType;
    }
}
