package com.example.demo.controller;

import com.example.demo.entity.Post;
import com.example.demo.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/posts")
public class PostController {

    @Autowired
    private PostService postService;

    @GetMapping
    public String getAllPosts(Model model) {
        List<Post> posts = postService.getAllPosts();
        model.addAttribute("posts", posts);
        return "posts"; // posts.html로 이동
    }

    @PostMapping
    public String createPost(@ModelAttribute Post post) {
        postService.createPost(post);
        return "redirect:/posts"; // 게시물 생성 후 목록으로 리다이렉트
    }

    @GetMapping("/delete/{id}")
    public String deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return "redirect:/posts"; // 게시물 삭제 후 목록으로 리다이렉트
    }

    // 게시물 수정 페이지 표시
    @GetMapping("/edit/{id}")
    public String editPost(@PathVariable Long id, Model model) {
        Post post = postService.getPostById(id).orElseThrow(() -> new RuntimeException("Post not found"));
        model.addAttribute("post", post);
        return "edit"; // edit.html로 이동
    }

    // 게시물 업데이트 처리
    @PostMapping("/edit")
    public String updatePost(@ModelAttribute Post post) {
        postService.createPost(post); // 같은 메서드를 사용하여 업데이트
        return "redirect:/posts"; // 업데이트 후 목록으로 리다이렉트
    }
}
