package io.blockwavelabs.tree.service;

import io.blockwavelabs.tree.auth.domain.LanguageEnum;
import io.blockwavelabs.tree.auth.domain.Role;
import io.blockwavelabs.tree.auth.domain.SocialPlatformType;
import io.blockwavelabs.tree.auth.domain.User;
import io.blockwavelabs.tree.auth.infrastructure.UserRepository;
import io.blockwavelabs.tree.config.auth.S3Uploader;
import io.blockwavelabs.tree.config.security.JwtTokenProvider;
import io.blockwavelabs.tree.domain.link.Link;
import io.blockwavelabs.tree.domain.link.LinkRepository;
import io.blockwavelabs.tree.domain.profileDecorate.ProfileDecorate;
import io.blockwavelabs.tree.domain.profileDecorate.ProfileDecorateRepository;
import io.blockwavelabs.tree.domain.user.refreshtoken.RefreshToken;
import io.blockwavelabs.tree.domain.user.refreshtoken.RefreshTokenRepository;
import io.blockwavelabs.tree.domain.userWallet.UserWalletRepository;
import io.blockwavelabs.tree.dto.profile.request.ProfileRequestDto;
import io.blockwavelabs.tree.dto.user.legacy.LoginReqDto;
import io.blockwavelabs.tree.dto.user.response.UserResponseDto;
import io.blockwavelabs.tree.dto.wallet.WalletInfoDto;
import io.blockwavelabs.tree.exception.SamTreeException;
import io.blockwavelabs.tree.exception.type.JwtExceptionType;
import io.blockwavelabs.tree.exception.type.UserExceptionType;
import io.blockwavelabs.tree.service.userdetails.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
//@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailsService customUserDetailsService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final S3Uploader s3Uploader;
    private final ProfileDecorateRepository profileDecorateRepository;
    private final LinkRepository linkRepository;
    private final UserWalletRepository userWalletRepository;

    @Value("${secret.tmp-delete-key}")
    private String secretKey;

    @Transactional(readOnly = true)
    public void validateDuplicateUserId(String userId){
        if (userRepository.existsByUserId(userId)){
            throw new SamTreeException(UserExceptionType.DUPLICATE_USER);
        }
    }

    @Transactional
    public User findUserByUserId(String userId){
        Optional<User> findUser = userRepository.findByUserId(userId);
        if(!findUser.isPresent()){
            throw new IllegalStateException("존재하지 않는 유저입니다.");
        }
        return findUser.get();
    }

    @Transactional(readOnly = true)
    public boolean getExistsUser(LoginReqDto loginReqDto){
        return userRepository.existsBySocialId(loginReqDto.getEmail());
    }

    @Transactional(readOnly = true)
    public UserResponseDto.TokenDto issuingToken(String email){
        User user = customUserDetailsService.getUser(email);

        String accessToken = jwtTokenProvider.createAccessToken(user.getSocialId(), user.getRole());
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getSocialId(), user.getRole());

        // refreshToken 은 DB에 저장
        if (refreshTokenRepository.existsByTokenKey(user.getSocialId())){
            refreshTokenRepository.updateRefreshTokenByKey(user.getSocialId(), refreshToken);
        } else {
            refreshTokenRepository.save(RefreshToken.builder()
                    .tokenKey(user.getSocialId())
                    .tokenValue(refreshToken)
                    .build());
        }

        return jwtTokenProvider.createTokenDTO(accessToken, refreshToken);
    }


    @Transactional
    public UserResponseDto.TokenDto reissuingToken(UserResponseDto.TokenDto tokenDto){
        String originAccessToken = tokenDto.getAccessToken();
        String originRefreshToken = tokenDto.getRefreshToken();

        /**
         * case1 : access token과 refresh token 모두가 만료된 경우 → 에러 발생 (재 로그인하여 둘다 새로 발급)
         * case2 : access token은 만료됐지만, refresh token은 유효한 경우 →  refresh token을 검증하여 access token 재발급
         * case3 : access token은 유효하지만, refresh token은 만료된 경우 →  access token을 검증하여 refresh token 재발급
         * case4 : access token과 refresh token 모두가 유효한 경우 → 정상 처리 () gg
         * */
        int accessTokenFlag = jwtTokenProvider.validateToken(originAccessToken);
        int refreshTokenFlag = jwtTokenProvider.validateToken(originRefreshToken);

        Authentication authentication = jwtTokenProvider.getAuthentication(originAccessToken);
        log.debug("Authentication = {}",authentication);

        if (accessTokenFlag == 2 && refreshTokenFlag == 2){
            throw new SamTreeException(JwtExceptionType.ALL_TOKENS_EXPIRED);
        } else if ( accessTokenFlag == 2 && refreshTokenFlag == 1) {
            refreshTokenRepository.findRefreshTokenByTokenValue(originRefreshToken)
                    .orElseThrow(() -> new SamTreeException(JwtExceptionType.INVALID_REFRESH_TOKEN));

            String socialId = jwtTokenProvider.getUserEmailByToken(originAccessToken);
            User user = customUserDetailsService.getUser(socialId);

            String newAccessToken = jwtTokenProvider.createAccessToken(socialId, user.getRole());
            String newRefreshToken = jwtTokenProvider.createRefreshToken(socialId, user.getRole());
            UserResponseDto.TokenDto case2TokenDto = jwtTokenProvider.createTokenDTO(newAccessToken, newRefreshToken);

            refreshTokenRepository.updateRefreshTokenByKey(socialId, newRefreshToken);
            return case2TokenDto;

        } else if ( accessTokenFlag == 1 && refreshTokenFlag == 2) {
            String socialId = jwtTokenProvider.getUserEmailByToken(originAccessToken);
            User user = customUserDetailsService.getUser(socialId);

            String newRefreshToken = jwtTokenProvider.createRefreshToken(socialId, user.getRole());
            refreshTokenRepository.updateRefreshTokenByKey(authentication.getName(), newRefreshToken);

            UserResponseDto.TokenDto case3TokenDto = jwtTokenProvider.createTokenDTO(originAccessToken, newRefreshToken);
            return case3TokenDto;
        } else {
            log.info("TOKEN IS VALID");
            return tokenDto;
        }
    }

    @Transactional
    public UserResponseDto.TotalInfo editingUserProfile(User user, MultipartFile image, ProfileRequestDto.ProfileEditDto profileEditDto) throws IOException {
//        String img = image.isEmpty() ? user.getProfileImg() : s3Uploader.upload(image, "media/users/profileImg");
        String img = Optional.ofNullable(image).map(multipartFile -> {
            try {
                return s3Uploader.upload(multipartFile, "media/users/profileImg");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }).orElse(user.getProfileImg());
        String profileName = profileEditDto.getProfileName().isEmpty() ? user.getProfileName() : profileEditDto.getProfileName();
        String profileDescription = profileEditDto.getProfileDescription().isEmpty() ? user.getProfileBio() : profileEditDto.getProfileDescription();

        userRepository.updateUserProfile(user.getUserId(), img, profileName, profileDescription);
        return UserResponseDto.TotalInfo.of(userRepository.findByUserId(user.getUserId()).get());
    }

    @Transactional
    public void editingUserLanguage(User user, String language){
        LanguageEnum checkLanguage = language.equals("KOR") ? LanguageEnum.KOR : LanguageEnum.ENG;
        userRepository.updateUserLanguage(user.getUserId(), checkLanguage);
    }

    @Transactional(readOnly = true)
    public UserResponseDto.UserTotalInfo getUserAllInfo(String userId){
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new SamTreeException(UserExceptionType.NOT_FOUND_USER));

        ProfileDecorate profileDecorate = profileDecorateRepository.findByUserId(userId)
                .orElseThrow(() -> new SamTreeException(UserExceptionType.NOT_FOUND_INFO));

        List<Link> links = linkRepository.findAllByUser_UserId(userId)
                .orElseThrow(() -> new SamTreeException(UserExceptionType.NOT_FOUND_INFO));

        List<WalletInfoDto> walletInfoList = new ArrayList<>();


        userWalletRepository.findAllByUser_UserId(userId)
                .orElseThrow(() -> new SamTreeException(UserExceptionType.NOT_FOUND_INFO))
                .stream()
                .forEach(userWallet -> {
                    walletInfoList.add(WalletInfoDto.of(userWallet.getWallet()));
                });

        return UserResponseDto.UserTotalInfo.builder()
                .user(UserResponseDto.TotalInfo.of(user))
                .profileDecorate(profileDecorate)
                .links(links)
                .wallets(walletInfoList)
                .build();
    }

    @Transactional
    public void changeUserId(User user, String newId){
        if (userRepository.findByUserId(newId).isPresent()){
            throw new SamTreeException(UserExceptionType.DUPLICATE_ID);
        }
        userRepository.updateUserId(user.getUserId(), newId);
    }

    @Transactional
    public void deleteUser(String userId, String password){
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new SamTreeException(UserExceptionType.NOT_FOUND_USER));

        System.out.println(secretKey);
        if (!password.equals(secretKey)){
            throw new SamTreeException(UserExceptionType.ILLEGAL_REQUEST);
        }
        userRepository.deleteUserByUserId(userId);
    }

    @Transactional(readOnly = true)
    public boolean checkDidExist(String did){
        return userRepository.findByDid(did).isPresent();
    }

    @Transactional
    public void saveFrontKey(String did, String frontKey){
        User user = userRepository.findByDid(did)
                .orElseThrow(() -> new SamTreeException(UserExceptionType.NOT_FOUND_USER));
        userRepository.updateFrontKey(user.getUserId(), frontKey);
    }

    @Transactional
    public void createUserByDid(String did2, String frontKey) {
        User user = User.builder()
                .userId(did2.substring(did2.length() - 10))
                .socialPlatform(SocialPlatformType.POLYGONID)
                .socialId(did2)
                .profileName(did2.substring(did2.length() - 10))
                .role(Role.USER)
                .profileBio(did2.substring(did2.length() - 10) + "'s 3Tree page :)")
                .profileImg("https://3tree-bucket.s3.ap-northeast-2.amazonaws.com/static/profile-defualt.png")
                .build();
        userRepository.save(user);
    }

    @Transactional(readOnly = true)
   public UserResponseDto.TotalInfoIncludeDid getUserByFrontKey(String frontKey){
        User user = userRepository.findByFrontKey(frontKey)
                .orElseThrow(() -> new SamTreeException(UserExceptionType.NOT_FOUND_USER));
        return UserResponseDto.TotalInfoIncludeDid.of(user);
    }
}
