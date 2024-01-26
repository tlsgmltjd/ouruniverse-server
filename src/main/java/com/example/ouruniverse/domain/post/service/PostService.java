package com.example.ouruniverse.domain.post.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.example.ouruniverse.domain.post.controller.dto.PostCreateRequest;
import com.example.ouruniverse.domain.post.controller.dto.PostCreateResponse;
import com.example.ouruniverse.domain.post.controller.dto.PostFindResponse;
import com.example.ouruniverse.domain.post.controller.dto.PostResponseDto;
import com.example.ouruniverse.domain.post.entity.PostEntity;
import com.example.ouruniverse.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.PageRequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class PostService {
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final PostRepository postRepository;
    private final AmazonS3 amazonS3;

    @Transactional
    public PostCreateResponse upload(List<MultipartFile> images, PostCreateRequest request) throws IOException {

        if (images.size() > 3 || images.isEmpty()) throw new RuntimeException();

        List<String> imgUrls = new ArrayList<>();

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

            imgUrls.add(amazonS3.getUrl(bucket, s3FileName).toString());
        }

        PostEntity newPost = PostEntity.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .imgUrls(imgUrls)
                .build();

        PostEntity savedEntity = postRepository.save(newPost);

        return PostCreateResponse.builder()
                .id(savedEntity.getId())
                .title(savedEntity.getTitle())
                .content(savedEntity.getContent())
                .imgUrls(savedEntity.getImgUrls())
                .build();
    }

    public PostFindResponse postFind(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<PostEntity> postEntityPage = postRepository.findAll(pageRequest);

        return PostFindResponse.builder()
                .last(postEntityPage.isLast())
                .response(
                        postEntityPage.getContent().stream()
                                .map(post -> PostResponseDto.builder()
                                        .id(post.getId())
                                        .title(post.getTitle())
                                        .content(post.getContent())
                                        .imgUrls(post.getImgUrls())
                                        .createdDate(post.getCreatedDate())
                                        .build())
                                .toList()
                ).build();
    }
}