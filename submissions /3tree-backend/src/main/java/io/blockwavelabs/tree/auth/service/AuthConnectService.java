package io.blockwavelabs.tree.auth.service;

import io.blockwavelabs.tree.auth.service.dto.request.LoginRequestDto;
import io.blockwavelabs.tree.auth.service.dto.response.LoginResponseDto;
import io.blockwavelabs.tree.external.client.twitter.dto.response.TwitterAccessTokenResponseDto;

public interface AuthConnectService {
    LoginResponseDto login(LoginRequestDto request, String userId);

    void disconnect(Long userId);

    TwitterAccessTokenResponseDto refresh(Long userId);
}
