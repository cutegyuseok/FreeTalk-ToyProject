package com.cutegyuseok.freetalk.global.aws;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Api(tags = {"AWS"}, description = "AWS S3 관련 서비스")
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AwsS3Controller {
    private final AwsS3Service awsS3Service;

    @PostMapping("/image")
    @ApiOperation(value = "이미지 업로드 API", notes = "이미지를 업로드 하고, URL 을 반환.\n\n" +
            "code: 200 업로드됨, 400 업로드 중 문제 발생, 404 파일이 비어있음")
    public ResponseEntity<?> uploadFile(
            @RequestParam("category") String category,
            @RequestPart(value = "file") MultipartFile multipartFile) {
        return awsS3Service.uploadFileV1(category, multipartFile);
    }

}
