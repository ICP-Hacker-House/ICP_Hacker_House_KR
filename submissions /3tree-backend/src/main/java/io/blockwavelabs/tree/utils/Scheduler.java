package io.blockwavelabs.tree.utils;

import io.blockwavelabs.tree.domain.tokenSendTrx.TokenSendTransaction;
import io.blockwavelabs.tree.domain.tokenSendTrx.TokenSendTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
@RequiredArgsConstructor
public class Scheduler {
    private final TokenSendTransactionRepository tokenSendtrxRepository;

    @Scheduled(cron = "0 0 0 * * *")
    public void checkTrxLinkExpiredEveryMidnight(){
        tokenSendtrxRepository.updateExpired();
    }
}
