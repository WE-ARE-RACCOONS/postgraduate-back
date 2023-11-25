package com.postgraduate.global.config.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.postgraduate.domain.image.exception.UploadException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3UploadService {

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    @Value("${cloud.aws.s3.prefix-profile}")
    private String profile;
    @Value("${cloud.aws.s3.prefix-certification}")
    private String certification;

    public String saveProfileFile(MultipartFile multipartFile) {
        return getString(multipartFile, profile);
    }

    public String saveCertificationFile(MultipartFile multipartFile) {
        return getString(multipartFile, certification);
    }

    private String getString(MultipartFile multipartFile, String dir) {
        try {
            String originalFilename = multipartFile.getOriginalFilename();
            String fileName = dir +"/" + originalFilename + UUID.randomUUID();

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(multipartFile.getSize());
            metadata.setContentType(multipartFile.getContentType());

            amazonS3.putObject(bucket, fileName, multipartFile.getInputStream(), metadata);
            return amazonS3.getUrl(bucket, fileName).toString();
        } catch (IOException ex) {
            throw new UploadException();
        }
    }

    public void deleteCertificationImage(String fileName)  {
        amazonS3.deleteObject(bucket+"/"+certification, fileName);
    }

    public void deleteProfileImage(String fileName)  {
        amazonS3.deleteObject(bucket+"/"+profile, fileName);
    }
}
