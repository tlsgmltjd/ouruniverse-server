package com.example.ouruniverse.domain.post.controller;

import com.example.ouruniverse.domain.post.controller.dto.PostRequest;
import com.example.ouruniverse.domain.post.controller.dto.PostResponse;
import com.example.ouruniverse.domain.post.entity.PostEntity;
import com.example.ouruniverse.domain.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {
    private final PostService postService;

    @PostMapping(consumes = { "multipart/form-data" })
    public ResponseEntity<PostResponse> createPost(
            @RequestPart(value = "images") List<MultipartFile> multipartFile,
            @RequestPart PostRequest request) throws IOException {
        return ResponseEntity.ok(
                postService.upload(multipartFile, request)
        );
    }

    @GetMapping
    public ResponseEntity<Page<PostEntity>> findPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(postService.postFind(page, size));
    }
}
