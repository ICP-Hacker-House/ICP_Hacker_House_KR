package io.blockwavelabs.tree.controller;

import com.google.gson.Gson;
import io.blockwavelabs.tree.auth.service.SocialUserService;
import io.blockwavelabs.tree.common.dto.ApiResponseDto;
import io.blockwavelabs.tree.domain.profileDecorate.ProfileDecorate;
import io.blockwavelabs.tree.dto.profile.request.ProfileRequestDto;
import io.blockwavelabs.tree.dto.profile.response.SocialAccountResponseDto;
import io.blockwavelabs.tree.dto.utils.ResultDto;
import io.blockwavelabs.tree.exception.SuccessStatus;
import io.blockwavelabs.tree.service.ProfileService;
import io.blockwavelabs.tree.service.userdetails.UserAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ProfileController {
    private final ProfileService profileService;
    private final SocialUserService socialUserService;

    @GetMapping(value = "/public/profile")
    public ResponseEntity<ResultDto<ProfileDecorate>> getProfileInfo(@RequestParam(value = "user_id")String userId){
        ProfileDecorate profileInfo = profileService.getProfileInfo(userId);
        return ResponseEntity.ok(ResultDto.res(HttpStatus.OK, HttpStatus.OK.toString(), profileInfo));
    }

    @PutMapping(value = "/profile/edit")
    public ResponseEntity<ResultDto<ProfileDecorate>> editProfileDecorate(@AuthenticationPrincipal UserAdapter userAdapter,
                                      @RequestPart(value = "image", required = false) MultipartFile image,
                                      @RequestParam(value = "data", required = false) String data) throws IOException {
        ProfileRequestDto.ProfileDecorateEditDto profileDecorateEditDto
                = new Gson().fromJson(data, ProfileRequestDto.ProfileDecorateEditDto.class);
        String userId = userAdapter.getUser().getUserId();
        ProfileDecorate profileDecorate = profileService.editProfile(userId, profileDecorateEditDto, image);
        return ResponseEntity.ok(ResultDto.res(HttpStatus.OK, HttpStatus.OK.toString(), profileDecorate));
    }

    @GetMapping("/profile/social")
    public ApiResponseDto<List<SocialAccountResponseDto>> getSocialAccountInformation(@AuthenticationPrincipal UserAdapter userAdapter) {
        return ApiResponseDto.success(SuccessStatus.GET_SOCIAL_ACCOUNT_LIST_SUCCESS, socialUserService.getSocialAccountList(userAdapter.getUser().getId()));
    }
}
