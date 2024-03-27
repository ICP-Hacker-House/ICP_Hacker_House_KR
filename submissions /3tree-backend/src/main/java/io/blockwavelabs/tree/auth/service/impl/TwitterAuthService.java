package io.blockwavelabs.tree.auth.service.impl;

import io.blockwavelabs.tree.auth.domain.SocialPlatformType;
import io.blockwavelabs.tree.auth.domain.SocialUser;
import io.blockwavelabs.tree.auth.domain.User;
import io.blockwavelabs.tree.auth.infrastructure.SocialUserRepository;
import io.blockwavelabs.tree.auth.infrastructure.UserRepository;
import io.blockwavelabs.tree.auth.service.AuthConnectService;
import io.blockwavelabs.tree.auth.service.dto.request.LoginRequestDto;
import io.blockwavelabs.tree.auth.service.dto.response.LoginResponseDto;
import io.blockwavelabs.tree.exception.SamTreeException;
import io.blockwavelabs.tree.exception.type.UserExceptionType;
import io.blockwavelabs.tree.external.client.twitter.TwitterApiClient;
import io.blockwavelabs.tree.external.client.twitter.TwitterApiHelper;
import io.blockwavelabs.tree.external.client.twitter.dto.response.TwitterAccessTokenResponseDto;
import io.blockwavelabs.tree.external.client.twitter.dto.response.TwitterUserProfileResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class TwitterAuthService implements AuthConnectService {

    private static final SocialPlatformType PLATFORM_TYPE = SocialPlatformType.TWITTER;

    private final TwitterApiClient twitterApiClient;

    private final SocialUserRepository socialUserRepository;
    private final UserRepository userRepository;

    @Value("${twitter.redirect.uri}")
    private String redirectUri;

    @Value("${twitter.codeVerifier}")
    private String codeVerifier;

    @Override
    @Transactional
    public LoginResponseDto login(LoginRequestDto request, String userId) {

        // Twitter OAuth2.0 Access 토큰 발급
        TwitterAccessTokenResponseDto accessTokenResponseDto = twitterApiClient.getAccessToken(
                TwitterApiHelper.makeBearerToken(),
                request.getCode(),
                "authorization_code",
                redirectUri,
                codeVerifier
        );

        // Twitter user profile 조회
        TwitterUserProfileResponseDto twitterUserProfileResponseDto = twitterApiClient.getUserProfile(
                "Bearer " + accessTokenResponseDto.getAccessToken()
        );

        // 기존에 트위터 계정을 연동한 적이 있는 경우
        if (socialUserRepository.existsBySocialId(twitterUserProfileResponseDto.getData().getId())) {
            SocialUser socialUser = socialUserRepository.findBySocialId(twitterUserProfileResponseDto.getData().getId())
                    .orElseThrow(() -> new SamTreeException(UserExceptionType.NOT_FOUND_USER));

            socialUser.updateAccountState(true, accessTokenResponseDto.getAccessToken(), accessTokenResponseDto.getRefreshToken());
            return LoginResponseDto.of(socialUser.getUsername());
        }

        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new SamTreeException(UserExceptionType.NOT_FOUND_USER));

        // 최초 연동
        socialUserRepository.save(SocialUser.newInstance(
                user,
                twitterUserProfileResponseDto.getData().getId(),
                twitterUserProfileResponseDto.getData().getName(),
                twitterUserProfileResponseDto.getData().getUsername(),
                accessTokenResponseDto.getAccessToken(),
                accessTokenResponseDto.getRefreshToken(),
                SocialPlatformType.TWITTER
        ));

        SocialUser twitterUser = socialUserRepository.findBySocialId(twitterUserProfileResponseDto.getData().getId())
                .orElseThrow(() -> new RuntimeException("asd"));

        return LoginResponseDto.of(twitterUser.getUsername());
    }

    @Override
    @Transactional
    public void disconnect(Long userId) {

        SocialUser socialUser = socialUserRepository.findByUserIdAndSocialPlatformType(userId, PLATFORM_TYPE)
                .orElseThrow(() -> new SamTreeException(UserExceptionType.NOT_FOUND_USER));

        socialUser.updateAccountState(false, "", "");
    }

    @Override
    @Transactional
    public TwitterAccessTokenResponseDto refresh(Long userId) {

        SocialUser socialUser = socialUserRepository.findByUserIdAndSocialPlatformType(userId, PLATFORM_TYPE)
                .orElseThrow(() -> new SamTreeException(UserExceptionType.NOT_FOUND_USER));

        TwitterAccessTokenResponseDto accessTokenResponseDto = twitterApiClient.getAccessTokenByRefreshToken(
                TwitterApiHelper.makeBearerToken(),
                "refresh_token",
                socialUser.getRefreshToken()
        );

        socialUser.updateAccountState(true, accessTokenResponseDto.getAccessToken(), accessTokenResponseDto.getRefreshToken());

        return accessTokenResponseDto;
    }
}
