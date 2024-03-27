package io.blockwavelabs.tree.external.client.twitter;

import io.blockwavelabs.tree.auth.domain.SocialPlatformType;
import io.blockwavelabs.tree.auth.domain.SocialUser;
import io.blockwavelabs.tree.auth.infrastructure.SocialUserRepository;
import io.blockwavelabs.tree.dto.twitter.TweetDto;
import io.blockwavelabs.tree.dto.twitter.TweetResultDto;
import io.blockwavelabs.tree.exception.SamTreeException;
import io.blockwavelabs.tree.exception.type.UserExceptionType;
import io.blockwavelabs.tree.external.client.twitter.dto.response.CreateTweetResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Locale;

@Component
@RequiredArgsConstructor
public class TwitterApiHelper {

    private final SocialUserRepository socialUserRepository;
    private final TwitterApiClient twitterApiClient;

    private static String clientId;

    private static String clientSecret;

    @Value("${twitter.clientId}")
    public void setClientId(String value) {
        clientId = value;
    }

    @Value("${twitter.clientSecret}")
    public void setClientSecret(String value) {
        clientSecret = value;
    }

    public static String makeBearerToken() {
        String bearerToken = clientId + ":" + clientSecret;
        return "Basic " + Base64.getEncoder().encodeToString(bearerToken.getBytes());
    }

    public CreateTweetResponseDto createTweet(Long userId, TweetDto request) {

        SocialUser socialUser = socialUserRepository.findByUserIdAndSocialPlatformType(userId, SocialPlatformType.TWITTER)
                .orElseThrow(() -> new SamTreeException(UserExceptionType.NOT_FOUND_USER));

        StringBuilder sb = new StringBuilder();

        switch (socialUser.getUser().getLanguage()) {
            case KOR:
                sb
                        .append("3TREE를 통해 @").append(request.getSenderId()).append("에게서 ").append(request.getTokenAmount()).append(" ").append(request.getTokenTicker()).append("를 받았어요 🥳\n")
                        .append("\n")
                        .append("⏰ 받은 시간 | ").append(request.getReceivedTime())
                        .append("💰 토큰 수량 | ").append(request.getTokenAmount()).append(" ").append(request.getTokenTicker()).append("\n")
                        .append("시세를 알고 싶다면? https://coinmarketcap.com/ko/").append("\n")
                        .append("https://3tree.io");
                break;

            case ENG:
                sb
                        .append("I received ").append(request.getTokenAmount()).append(" ").append(request.getTokenTicker()).append(" from @").append(request.getSenderId()).append(" via 3TREE 🥳\n")
                        .append("\n")
                        .append("⏰ Receive Time | ").append(request.getReceivedTime()).append("\n")
                        .append("💰 Token Amount | ").append(request.getTokenAmount()).append(" ").append(request.getTokenTicker()).append("\n")
                        .append("If you want to know the market price? https://coinmarketcap.com/ko/").append("\n")
                        .append("https://3tree.io");
        }
        return twitterApiClient.createTweet("Bearer " + socialUser.getAccessToken(), new TweetResultDto(sb.toString()));
    }
}
