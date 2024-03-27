package io.blockwavelabs.tree.controller;

import io.blockwavelabs.tree.domain.wallet.WalletTypeEnum;
import io.blockwavelabs.tree.dto.utils.ResultDto;
import io.blockwavelabs.tree.dto.wallet.UserWalletInfoDto;
import io.blockwavelabs.tree.dto.wallet.WalletInfoDto;
import io.blockwavelabs.tree.service.WalletService;
import io.blockwavelabs.tree.service.userdetails.UserAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@Slf4j
public class WalletController {
    private final WalletService walletService;

    /*
     * 새 지갑 추가
     */
    @PostMapping(value = "/wallets/new")
    public ResponseEntity<ResultDto<WalletInfoDto>> addWallet(@AuthenticationPrincipal UserAdapter userAdapter, @RequestBody WalletInfoDto walletInfoDto){
        System.out.println(walletInfoDto.getWalletType());
        WalletInfoDto responseWalletInfoDto = walletService.saveWallet(userAdapter.getUser().getId(), WalletTypeEnum.valueOf(walletInfoDto.getWalletType()), walletInfoDto.getWalletAddress());
        return ResponseEntity.ok().body(ResultDto.res(HttpStatus.OK, HttpStatus.OK.toString(), responseWalletInfoDto));
    }

    /*
     * 지갑 전체 불러오기
     */
    @GetMapping(value= "public/wallets/all")
    public ResponseEntity<ResultDto<List<UserWalletInfoDto>>> getAllWallets(@RequestParam(value="userId") String userId){
        List<UserWalletInfoDto> userWalletsList = walletService.getAllUserWallets(userId);
        return ResponseEntity.ok().body(ResultDto.res(HttpStatus.OK, HttpStatus.OK.toString(), userWalletsList));
    }

    /*
     * 지갑 삭제
     */
    @DeleteMapping(value="wallets")
    public ResponseEntity<ResultDto<String>> deleteUserWallet(@AuthenticationPrincipal UserAdapter userAdapter, @RequestParam("user_wallet_index") Long userWalletIndex){
        Long userIndex = userAdapter.getUser().getId();;
        walletService.deleteUserWallet(userIndex, userWalletIndex);
        return ResponseEntity.ok(ResultDto.res(HttpStatus.OK, HttpStatus.OK.toString(), "Delete User Wallet SuccessFully"));
    }
}
