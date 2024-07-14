package com.example.demo.aws.s3;



import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.demo.Config.AmazonConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class AmazonS3Manager {
    private final AmazonS3 amazonS3;
    private final AmazonConfig amazonConfig;

    public String uploadFile(MultipartFile file) {
        String keyName = generateFileKeyName(file.getOriginalFilename());
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        try {
            amazonS3.putObject(new PutObjectRequest(amazonConfig.getBucket(), keyName, file.getInputStream(), metadata));
        } catch (IOException e) {
            log.error("Error at AmazonS3Manager uploadFile : {}", e.getMessage(), e);
            throw new RuntimeException("Failed to upload file", e);
        }
        return amazonS3.getUrl(amazonConfig.getBucket(), keyName).toString();
    }

    private String generateFileKeyName(String originalFilename) {
        String uuid = UUID.randomUUID().toString();
        return "profile/" + uuid + "_" + originalFilename;
    }
}

