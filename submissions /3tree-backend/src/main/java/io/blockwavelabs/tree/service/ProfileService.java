package io.blockwavelabs.tree.service;

import io.blockwavelabs.tree.config.auth.S3Uploader;
import io.blockwavelabs.tree.domain.profileDecorate.BackgroundTypeEnum;
import io.blockwavelabs.tree.domain.profileDecorate.ProfileDecorate;
import io.blockwavelabs.tree.domain.profileDecorate.ProfileDecorateRepository;
import io.blockwavelabs.tree.dto.profile.request.ProfileRequestDto;
import io.blockwavelabs.tree.exception.SamTreeException;
import io.blockwavelabs.tree.exception.type.UserExceptionType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProfileService {
    private final ProfileDecorateRepository profileDecorateRepository;
    private final S3Uploader s3Uploader;


    @Transactional(readOnly = true)
    public ProfileDecorate getProfileInfo(String userId){
        return profileDecorateRepository.findByUserId(userId)
                .orElseThrow(() -> new SamTreeException(UserExceptionType.NOT_FOUND_INFO));
    }

    @Transactional
    public ProfileDecorate editProfile(String userId, ProfileRequestDto.ProfileDecorateEditDto profileDecorateEditDto, MultipartFile image) throws IOException {
        ProfileDecorate profileDecorate = profileDecorateRepository.findByUserId(userId)
                .orElseThrow(() -> new SamTreeException(UserExceptionType.NOT_FOUND_INFO));

        //String backgroundType = image.isEmpty() ? BackgroundTypeEnum.COLOR.name() : BackgroundTypeEnum.IMAGE.name();
        //String imgUrl = image.isEmpty() ? null : s3Uploader.upload(image, "media/users/backgroundImg");

        String backgroundType = Optional.ofNullable(image).map(multipartFile -> BackgroundTypeEnum.IMAGE.name()).orElse(BackgroundTypeEnum.COLOR.name());

        String imgUrl = Optional.ofNullable(image).map(multipartFile -> {
            try {
                return s3Uploader.upload(multipartFile, "media/users/backgroundImg");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }).orElse(null);


        String backgroundColor = profileDecorateEditDto.getBackgroundColor().isEmpty() ?
                profileDecorate.getBackgroundColor() : profileDecorateEditDto.getBackgroundColor();
        String buttonColor = profileDecorateEditDto.getButtonColor().isEmpty() ?
                profileDecorate.getButtonColor() : profileDecorateEditDto.getButtonColor();
        String buttonFontColor = profileDecorateEditDto.getButtonFontColor().isEmpty() ?
                profileDecorate.getButtonFontColor() : profileDecorateEditDto.getButtonFontColor();
        String fontColor = profileDecorateEditDto.getFontColor().isEmpty() ?
                profileDecorate.getFontColor() : profileDecorateEditDto.getFontColor();

        profileDecorateRepository.updateProfileDecorate(backgroundType, backgroundColor, imgUrl, buttonColor, buttonFontColor, fontColor, profileDecorate.getId());

        ProfileDecorate response = profileDecorateRepository.findByUserId(userId)
                .orElseThrow(() -> new SamTreeException(UserExceptionType.NOT_FOUND_INFO));

        return response;
    }
}
