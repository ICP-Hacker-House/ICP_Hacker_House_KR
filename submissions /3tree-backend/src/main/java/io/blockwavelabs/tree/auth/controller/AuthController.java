package io.blockwavelabs.tree.auth.controller;

import io.blockwavelabs.tree.auth.controller.dto.request.SocialLoginDisconnectRequestDto;
import io.blockwavelabs.tree.auth.controller.dto.request.SocialLoginRequestDto;
import io.blockwavelabs.tree.auth.domain.SocialPlatformType;
import io.blockwavelabs.tree.auth.service.AuthConnectService;
import io.blockwavelabs.tree.auth.service.AuthConnectServiceProvider;
import io.blockwavelabs.tree.auth.service.dto.request.LoginRequestDto;
import io.blockwavelabs.tree.auth.service.dto.response.LoginResponseDto;
import io.blockwavelabs.tree.common.dto.ApiResponseDto;
import io.blockwavelabs.tree.exception.SuccessStatus;
import io.blockwavelabs.tree.external.client.twitter.dto.response.TwitterAccessTokenResponseDto;
import io.blockwavelabs.tree.service.userdetails.UserAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthConnectServiceProvider authConnectServiceProvider;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/connect")
    public ApiResponseDto<LoginResponseDto> connect(@AuthenticationPrincipal UserAdapter userAdapter, @RequestBody SocialLoginRequestDto request) {
        final AuthConnectService authConnectService = authConnectServiceProvider.getAuthService(request.getSocialPlatformType());
        return ApiResponseDto.success(
                SuccessStatus.SOCIAL_CONNECT_SUCCESS,
                authConnectService.login(
                        LoginRequestDto.of(request.getCode()),
                        userAdapter.getUser().getUserId()
                )
        );
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/disconnect")
    public ApiResponseDto disconnect(@AuthenticationPrincipal UserAdapter userAdapter, @RequestBody SocialLoginDisconnectRequestDto request) {
        final AuthConnectService authConnectService = authConnectServiceProvider.getAuthService(request.getSocialPlatformType());
        authConnectService.disconnect(userAdapter.getUser().getId());
        return ApiResponseDto.success(SuccessStatus.SOCIAL_DISCONNECT_SUCCESS);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/twitter/refresh")
    public ApiResponseDto<TwitterAccessTokenResponseDto> twitterTokenRefresh(@AuthenticationPrincipal UserAdapter userAdapter) {
        final AuthConnectService authConnectService = authConnectServiceProvider.getAuthService(SocialPlatformType.TWITTER);
        TwitterAccessTokenResponseDto response =authConnectService.refresh(userAdapter.getUser().getId());
        return ApiResponseDto.success(SuccessStatus.GET_TWITTER_TOKEN_SUCCESS, response);
    }
}
