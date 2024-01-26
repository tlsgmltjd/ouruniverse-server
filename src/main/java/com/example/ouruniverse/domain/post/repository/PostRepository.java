package com.example.ouruniverse.domain.post.repository;

import com.example.ouruniverse.domain.post.entity.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<PostEntity, Long> {
}
