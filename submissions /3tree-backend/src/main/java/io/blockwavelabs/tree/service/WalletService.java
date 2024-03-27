package io.blockwavelabs.tree.service;

import io.blockwavelabs.tree.auth.domain.User;
import io.blockwavelabs.tree.auth.infrastructure.UserRepository;
import io.blockwavelabs.tree.domain.userWallet.UserWallet;
import io.blockwavelabs.tree.domain.userWallet.UserWalletRepository;
import io.blockwavelabs.tree.domain.wallet.Wallet;
import io.blockwavelabs.tree.domain.wallet.WalletRepository;
import io.blockwavelabs.tree.domain.wallet.WalletTypeEnum;
import io.blockwavelabs.tree.dto.wallet.UserWalletInfoDto;
import io.blockwavelabs.tree.dto.wallet.WalletInfoDto;
import io.blockwavelabs.tree.exception.SamTreeException;
import io.blockwavelabs.tree.exception.type.UserExceptionType;
import io.blockwavelabs.tree.exception.type.WalletExceptionType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class WalletService {
    final private WalletRepository walletRepository;
    final private UserRepository userRepository;
    final private UserWalletRepository userWalletRepository;


    @Transactional  // 변경
    public WalletInfoDto saveWallet(Long userIndex, WalletTypeEnum walletType, String walletAddress){
        // 1. 지갑 entity 있는지 확인 -> 없으면 생성
        Wallet wallet;
        if(!walletRepository.existsByWalletAddress(walletAddress)){
            Wallet newWallet = new Wallet(walletAddress, walletType);
            wallet = walletRepository.saveAndFlush(newWallet);
        } else {
            wallet = walletRepository.findByWalletAddress(walletAddress);
        }
        User user = userRepository.getReferenceById(userIndex);
        // 2. userWallet 추가하기
        if(userWalletRepository.existsByWalletAndUser(wallet, user)){
            throw new SamTreeException(WalletExceptionType.WALLET_ALREADY_EXIST);
        }
        userWalletRepository.save(UserWallet.builder()
                .wallet(wallet)
                .user(user)
                .walletType(walletType)
                .build());
        return WalletInfoDto.of(wallet);
    }

    @Transactional(readOnly = true)
    public List<UserWalletInfoDto> getAllUserWallets(String userId){
        if(!userRepository.existsByUserId(userId)){
            throw new SamTreeException(UserExceptionType.NOT_FOUND_USER);
        }

        List<UserWalletInfoDto> allUserWallets = new ArrayList<>();
        userWalletRepository.findAllByUser_UserId(userId)
                .orElseThrow(() -> new SamTreeException(WalletExceptionType.NOT_FOUND_INFO))
                .stream()
                .forEach(UserWallet -> allUserWallets.add(UserWalletInfoDto.of(UserWallet)));
        return allUserWallets;
    }

    @Transactional
    public void deleteUserWallet(Long userIndex, Long userWalletIndex){
        UserWallet userWallet = userWalletRepository.findUserWalletById(userWalletIndex)
                .orElseThrow(() -> new SamTreeException(WalletExceptionType.NOT_FOUND_INFO));
        if(!userIndex.equals(userWallet.getUser().getId())){
            throw new SamTreeException(WalletExceptionType.USER_NOT_MATCHED);
        }
        userWalletRepository.deleteById(userWalletIndex);
        // 남은 지갑도 삭제
        if(userWallet.getWallet().getUserWallets().size()==0){
            walletRepository.deleteByWalletAddress(userWallet.getWallet().getWalletAddress());
        }
    }


}
