package io.blockwavelabs.tree.auth.service;

import io.blockwavelabs.tree.auth.domain.SocialUser;
import io.blockwavelabs.tree.auth.infrastructure.SocialUserRepository;
import io.blockwavelabs.tree.dto.profile.response.SocialAccountResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SocialUserService {

    private final SocialUserRepository socialUserRepository;

    @Transactional
    public List<SocialAccountResponseDto> getSocialAccountList(Long userId) {
        List<SocialUser> socialAccountList = socialUserRepository.findAllByUserIdAndIsConnected(userId, true);

        return socialAccountList.stream()
                .map(socialAccount -> SocialAccountResponseDto.of(
                        socialAccount.getSocialPlatformType(),
                        socialAccount.getUsername()
                )).collect(Collectors.toList());
    }
}
