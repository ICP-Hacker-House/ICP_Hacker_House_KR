package io.blockwavelabs.tree.controller;

import io.blockwavelabs.tree.config.auth.S3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
public class FileUploadController {

    private final S3Uploader s3Upload;

    @PostMapping("/s3/upload")
    public String uploadFile(@RequestParam("images") MultipartFile multipartFile, @RequestParam("dirName") String dirName) throws IOException {
        return s3Upload.upload(multipartFile, dirName);
    }
}