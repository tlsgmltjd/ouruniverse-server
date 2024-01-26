package com.example.ouruniverse.domain.post.controller;

import com.example.ouruniverse.domain.post.controller.dto.ImgUrlResponse;
import com.example.ouruniverse.domain.post.controller.dto.PostRequest;
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

    @PostMapping("/post")
    public ResponseEntity<List<ImgUrlResponse>> uploadFile(
            @RequestParam("images") List<MultipartFile> multipartFile,
            @RequestBody PostRequest request) throws IOException {
        return ResponseEntity.ok(
                postService.upload(multipartFile)
        );
    }

    @GetMapping("/post")
    public ResponseEntity<Void> getPosts() {
        return ResponseEntity.ok().build();
    }
}
