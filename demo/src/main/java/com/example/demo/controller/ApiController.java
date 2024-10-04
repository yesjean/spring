package com.example.demo.controller;

import com.example.demo.entity.Post;
import com.example.demo.repository.PostRepository;
import com.example.demo.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class ApiController {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostService postService;

    @GetMapping("/api/posts")
    public List<Post> getPosts() {
        return postRepository.findAll();
    }

    // 특정 포스트의 위치 정보 반환
    @GetMapping("/api/posts/{id}")
    public ResponseEntity<Post> getPostById(@PathVariable Long id) {
        Post post = postService.getPostById(id).orElseThrow(() -> new RuntimeException("Post not found"));
        return ResponseEntity.ok(post);
    }

    @PostMapping("/api/posts/{id}/like")
    public ResponseEntity<String> likePost(@PathVariable Long id) {
        Post post = postService.getPostById(id).orElseThrow(() -> new RuntimeException("Post not found"));
        if (post != null) {
            post.setLikes(post.getLikes() + 1);
            postService.savePost(post);
            return ResponseEntity.ok("Like added");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Post not found");
        }
    }
}
