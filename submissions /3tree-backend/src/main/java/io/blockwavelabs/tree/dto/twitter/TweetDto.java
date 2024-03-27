package io.blockwavelabs.tree.dto.twitter;

import lombok.*;

import java.time.LocalDateTime;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TweetDto {

    private String receivedTime;
    private String senderId;
    private Float tokenAmount;
    private String tokenTicker;
    private String walletAddress;
    private String trxHash;

    public static TweetDto of(String receivedTime, String senderId, Float tokenAmount, String tokenTicker, String walletAddress, String trxHash) {
        return new TweetDto(receivedTime, senderId, tokenAmount, tokenTicker, walletAddress, trxHash);
    }
}
