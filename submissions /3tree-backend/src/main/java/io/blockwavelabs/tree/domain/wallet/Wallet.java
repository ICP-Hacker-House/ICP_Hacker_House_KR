package io.blockwavelabs.tree.domain.wallet;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.blockwavelabs.tree.domain.userWallet.UserWallet;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
//@Setter
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor
public class Wallet {
    @Id
    private String walletAddress;

    @Enumerated(EnumType.STRING)
    private WalletTypeEnum walletType;

    @GeneratedValue
    private Long id;

    @JsonManagedReference
    @OneToMany(mappedBy = "wallet")
    private List<UserWallet> userWallets = new ArrayList<>();

    @Builder
    public Wallet(String walletAddress, WalletTypeEnum walletType){
        this.walletAddress = walletAddress;
        this.walletType = walletType;
    }
}
