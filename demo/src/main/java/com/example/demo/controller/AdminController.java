package com.example.demo.controller;

import com.example.demo.service.PostService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private PostService postService; // PostService 추가

    @GetMapping("/admin")
    public String adminDashboard(Model model) {
        model.addAttribute("users", userService.findAllUsers());
        model.addAttribute("posts", userService.findAllPosts());
        return "admin/dashboard"; // admin/dashboard.html
    }

    @PostMapping("/admin/deleteUser")
    public String deleteUser(@RequestParam Long userId) {
        userService.deleteUser(userId);
        return "redirect:/admin"; // 삭제 후 관리자 페이지로 리다이렉트
    }

    @PostMapping("/admin/deletePost")
    public String deletePost(@RequestParam Long postId) {
        postService.deletePost(postId);
        return "redirect:/admin"; // 삭제 후 관리자 페이지로 리다이렉트
    }
}
