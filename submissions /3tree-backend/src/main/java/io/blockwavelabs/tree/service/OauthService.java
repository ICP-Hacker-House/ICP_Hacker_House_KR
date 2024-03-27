package io.blockwavelabs.tree.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.blockwavelabs.tree.config.auth.GoogleOAuth;
import io.blockwavelabs.tree.auth.domain.Role;
import io.blockwavelabs.tree.auth.domain.SocialPlatformType;
import io.blockwavelabs.tree.auth.domain.User;
import io.blockwavelabs.tree.auth.infrastructure.UserRepository;
import io.blockwavelabs.tree.dto.oauth.GoogleUserInfoDto;
import io.blockwavelabs.tree.dto.user.response.UserResponseDto;
import io.blockwavelabs.tree.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class OauthService {
    private final GoogleOAuth googleOAuth;
    private final UserRepository userRepository;
    private final UserService userService;
    private final Utils utils;

    private GoogleUserInfoDto getGoogleUserInfoDto(String oAuthToken) throws JsonProcessingException {
        ResponseEntity<String> userInfoResponse = googleOAuth.requestUserInfo(oAuthToken);
        GoogleUserInfoDto googleUser = googleOAuth.getUserInfo(userInfoResponse);
        return googleUser;
    }

    @Transactional
    public UserResponseDto.UserLoginResponseDto googlelogin(String code) throws IOException {
        GoogleUserInfoDto googleUser = getGoogleUserInfoDto(code);
        String email = googleUser.getEmail();
        String name = googleUser.getName();
        //user ID 랜덤 생성
        String userId = utils.createDefaultUserIdWithEmail(email);
        String status = "LOGIN";

        // 첫 로그인시 사용자 정보를 저장
        if (!userRepository.existsBySocialId(email)) {
            status = "SIGNUP";
            userRepository.save(
                    User.builder()
                            .userId(userId)
                            .socialPlatform(SocialPlatformType.GOOGLE)
                            .socialId(email)
                            .profileImg(googleUser.getPicture())
                            .profileName(googleUser.getName())
                            .role(Role.USER)
                            .profileBio(userId + "'s 3Tree page :)")
                            .build()
            );
        }

        UserResponseDto.TokenDto tokenDto = userService.issuingToken(email);
        UserResponseDto.SocialLoginResponse socialLoginResponse = UserResponseDto.SocialLoginResponse.builder()
                .name(name).email(email).img(googleUser.getPicture()).status(status).userId(userId)
                .build();

        return UserResponseDto.UserLoginResponseDto.builder()
                .tokenDto(tokenDto)
                .socialLoginResponse(socialLoginResponse)
                .build();
    }

}
