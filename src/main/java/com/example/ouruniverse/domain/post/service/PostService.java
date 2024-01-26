package com.example.ouruniverse.domain.post.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.example.ouruniverse.domain.post.controller.dto.ImgUrlResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class PostService {
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final AmazonS3 amazonS3;

    public List<ImgUrlResponse> upload(List<MultipartFile> images) throws IOException {

        if (images.size() > 3 || images.isEmpty()) throw new RuntimeException();

        List<ImgUrlResponse> responses = new ArrayList<>();

        for (MultipartFile image : images) {
            if (image != null &&
                    !image.getOriginalFilename().toLowerCase().endsWith(".png") &&
                    !image.getOriginalFilename().toLowerCase().endsWith(".jpg") &&
                    !image.getOriginalFilename().toLowerCase().endsWith(".jpeg")) {
                throw new RuntimeException();
            }

            String s3FileName = UUID.randomUUID() + "-" + image.getOriginalFilename();

            ObjectMetadata objMeta = new ObjectMetadata();
            objMeta.setContentLength(image.getInputStream().available());
            objMeta.setContentType(image.getContentType());

            amazonS3.putObject(bucket, s3FileName, image.getInputStream(), objMeta);

            responses.add(new ImgUrlResponse(amazonS3.getUrl(bucket, s3FileName).toString()));
        }

        return responses;
    }
}