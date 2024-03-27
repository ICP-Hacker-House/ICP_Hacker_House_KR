package io.blockwavelabs.tree.controller;

import com.google.gson.Gson;
import io.blockwavelabs.tree.auth.domain.User;
import io.blockwavelabs.tree.dto.profile.request.ProfileRequestDto;
import io.blockwavelabs.tree.dto.user.request.UserRequestDto;
import io.blockwavelabs.tree.dto.user.response.UserResponseDto;
import io.blockwavelabs.tree.dto.utils.ResultDto;
import io.blockwavelabs.tree.service.OauthService;
import io.blockwavelabs.tree.service.UserService;
import io.blockwavelabs.tree.service.userdetails.UserAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping
public class UserController {
    private final UserService userService;
    private final OauthService oauthService;

    @GetMapping("/public/users/login/google")
    public ResponseEntity<ResultDto<UserResponseDto.UserLoginResponseDto>> googleLogin(
            @RequestParam(name = "code") String code) throws IOException {
        UserResponseDto.UserLoginResponseDto googleloginDto = oauthService.googlelogin(code);
        return ResponseEntity.ok(ResultDto.res(HttpStatus.OK, HttpStatus.OK.toString(), googleloginDto));
    }

    @PostMapping(value = "/public/users/reissue")
    public ResponseEntity<ResultDto<UserResponseDto.TokenDto>> reissuing(@RequestBody UserResponseDto.TokenDto tokenDto){
        UserResponseDto.TokenDto token = userService.reissuingToken(tokenDto);
        return ResponseEntity.ok(ResultDto.res(HttpStatus.OK, HttpStatus.OK.toString(), token));
    }

    @GetMapping(value = "/users/my/info")
    public ResponseEntity<ResultDto<UserResponseDto.TotalInfo>> userInfo(@AuthenticationPrincipal UserAdapter userAdapter) {
        UserResponseDto.TotalInfo totalInfo = UserResponseDto.TotalInfo.of(userAdapter.getUser());
        return ResponseEntity.ok(ResultDto.res(HttpStatus.OK, HttpStatus.OK.toString(), totalInfo));
    }

    @GetMapping(value = "/public/users/id/check")
    public ResponseEntity<ResultDto> checkUserId(@RequestParam(value = "user_id")String userId){
        userService.validateDuplicateUserId(userId);
        return ResponseEntity.ok(ResultDto.res(HttpStatus.OK, HttpStatus.OK.toString()));
    }

    @PutMapping(value = "/users/edit/profile")
    public ResponseEntity<ResultDto<UserResponseDto.TotalInfo>> editingProfile(@AuthenticationPrincipal UserAdapter userAdapter,
                                                                                @RequestParam(value = "image", required = false) MultipartFile image,
                                                                                @RequestParam(value = "profile",required = false) String data) throws IOException{
        ProfileRequestDto.ProfileEditDto profileEditDto = new Gson().fromJson(data, ProfileRequestDto.ProfileEditDto.class);
        User user = userAdapter.getUser();
        UserResponseDto.TotalInfo editUserInfo = userService.editingUserProfile(user, image, profileEditDto);
        return ResponseEntity.ok(ResultDto.res(HttpStatus.OK, HttpStatus.OK.toString(), editUserInfo));
    }

    @PutMapping(value = "/users/edit/language")
    public ResponseEntity<ResultDto> editingLanguage(@AuthenticationPrincipal UserAdapter userAdapter,
                                             @RequestParam(value = "language") String language){
        userService.editingUserLanguage(userAdapter.getUser(), language);
        return ResponseEntity.ok(ResultDto.res(HttpStatus.OK, HttpStatus.OK.toString()));
    }

    @GetMapping(value = "/public/users/info")
    public ResponseEntity<ResultDto<UserResponseDto.UserTotalInfo>> getOtherUserInfo(@RequestParam(value = "user_id")String userId){
        UserResponseDto.UserTotalInfo userAllInfo = userService.getUserAllInfo(userId);
        return ResponseEntity.ok(ResultDto.res(HttpStatus.OK, HttpStatus.OK.toString(), userAllInfo));
    }

    @PutMapping(value = "/users/edit/userid")
    public ResponseEntity<ResultDto> editingOwnUserId(@AuthenticationPrincipal UserAdapter userAdapter,
                                              @RequestParam(value = "new_id")String newId){
        userService.changeUserId(userAdapter.getUser(), newId);
        return ResponseEntity.ok(ResultDto.res(HttpStatus.OK, HttpStatus.OK.toString()));
    }


    // tmp -> 정식 api로 변경해야함
    @DeleteMapping(value = "/public/users/tmp")
    public ResponseEntity<ResultDto> deleteUser(@RequestParam(value = "user_id")String userId, @RequestParam(value="password") String password/*@AuthenticationPrincipal UserAdapter userAdapter*/){
        //userService.deleteUser(userAdapter.getUser());

        userService.deleteUser(userId, password);
        return ResponseEntity.ok(ResultDto.res(HttpStatus.OK, HttpStatus.OK.toString()));
    }

    @PostMapping("/public/polygon-id/did")
    public ResponseEntity<ResultDto> saveDid(@RequestBody UserRequestDto.PolygonDidDto didDto) {
        // 1. 해당 did가 이미 있는지 확인 입니다...
        boolean didExist = userService.checkDidExist(didDto.getDid());

        // 2. 이미 있다면 frontKey만 저장
        if (didExist) {
            userService.saveFrontKey(didDto.getDid(), didDto.getFrontKey());
            return ResponseEntity.ok(ResultDto.res(HttpStatus.OK, HttpStatus.OK.toString()));
        }
        else {
            userService.createUserByDid(didDto.getDid(), didDto.getFrontKey());
            return ResponseEntity.ok(ResultDto.res(HttpStatus.OK, HttpStatus.OK.toString()));
        }
    }

    @GetMapping("/public/polygon-id/did")
    public ResponseEntity<ResultDto<UserResponseDto.TotalInfoIncludeDid>> getUserByFrontKey(@RequestParam("frontKey") String frontKey){
        UserResponseDto.TotalInfoIncludeDid user = userService.getUserByFrontKey(frontKey);
        return ResponseEntity.ok(ResultDto.res(HttpStatus.OK, HttpStatus.OK.toString(), user));
    }

}
