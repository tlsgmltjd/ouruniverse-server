package com.example.ouruniverse.domain.post.controller;

import com.example.ouruniverse.domain.post.controller.dto.PostRequest;
import com.example.ouruniverse.domain.post.controller.dto.PostResponse;
import com.example.ouruniverse.domain.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping(value = "/post", consumes = { "multipart/form-data" })
    public ResponseEntity<PostResponse> uploadFile(
            @RequestPart(value = "images") List<MultipartFile> multipartFile,
            @RequestPart PostRequest request) throws IOException {
        return ResponseEntity.ok(
                postService.upload(multipartFile, request)
        );
    }

    @GetMapping("/post")
    public ResponseEntity<Void> getPosts() {
        return ResponseEntity.ok().build();
    }
}
