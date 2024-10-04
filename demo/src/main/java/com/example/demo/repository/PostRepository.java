package com.example.demo.repository;

import com.example.demo.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
    Post findTopByOrderByLikesDesc(); // 좋아요 수 기준으로 상위 1개 게시물 조회
}
