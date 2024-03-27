package io.blockwavelabs.tree.auth.service;

import io.blockwavelabs.tree.auth.domain.SocialPlatformType;
import io.blockwavelabs.tree.auth.service.impl.TwitterAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Component
public class AuthConnectServiceProvider {

    private static final Map<SocialPlatformType, AuthConnectService> authServiceMap = new HashMap<>();

    private final TwitterAuthService twitterAuthService;

    @PostConstruct
    void initializeAuthServicesMap() {
        authServiceMap.put(SocialPlatformType.TWITTER, twitterAuthService);
    }

    public AuthConnectService getAuthService(SocialPlatformType socialPlatformType) {
        return authServiceMap.get(socialPlatformType);
    }
}
