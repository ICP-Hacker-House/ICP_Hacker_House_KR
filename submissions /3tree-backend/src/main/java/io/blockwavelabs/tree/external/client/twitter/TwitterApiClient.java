package io.blockwavelabs.tree.external.client.twitter;

import io.blockwavelabs.tree.dto.twitter.TweetResultDto;
import io.blockwavelabs.tree.external.client.twitter.dto.response.CreateTweetResponseDto;
import io.blockwavelabs.tree.external.client.twitter.dto.response.TwitterAccessTokenResponseDto;
import io.blockwavelabs.tree.external.client.twitter.dto.response.TwitterUserProfileResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "twitterApiClient", url = "https://api.twitter.com")
public interface TwitterApiClient {

    @PostMapping(value = "/2/oauth2/token", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    TwitterAccessTokenResponseDto getAccessToken(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String bearerToken,
            @RequestParam("code") String code,
            @RequestParam("grant_type") String grantType,
            @RequestParam("redirect_uri") String redirectUri,
            @RequestParam("code_verifier") String codeVerifier
    );

    @GetMapping(value = "/2/users/me")
    TwitterUserProfileResponseDto getUserProfile(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String bearerToken
    );

    @PostMapping(value = "/2/tweets", consumes = MediaType.APPLICATION_JSON_VALUE)
    CreateTweetResponseDto createTweet(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String bearerToken,
            @RequestBody TweetResultDto text
    );

    @PostMapping(value = "/2/oauth2/token", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    TwitterAccessTokenResponseDto getAccessTokenByRefreshToken(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String bearerToken,
            @RequestParam("grant_type") String grantType,
            @RequestParam("refresh_token") String refreshToken
    );
}
