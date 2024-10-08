package com.example.demo.controller;

import com.example.demo.entity.Post;
import com.example.demo.repository.PostRepository;
import com.example.demo.service.EmailService;
import com.example.demo.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/posts")
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepository;

    // 이미지 저장 경로
    private final String uploadDir = "src/main/resources/static/images/";

    @GetMapping("/api/posts")
    public List<Post> getApiPosts() {
        return postService.getAllPosts();
    }
    @GetMapping
    public String getAllPosts(Model model) {
        List<Post> allPosts = postService.getAllPosts(); // 전체 게시물
        Post topPost = (allPosts.isEmpty()) ? null: postService.getPostWithMostLikes(); // 좋아요 수가 가장 많은 게시물

        model.addAttribute("topPost", topPost);
        model.addAttribute("posts", allPosts);
        return "posts"; // posts.html 템플릿으로 이동
    }

    // 게시물 생성 페이지로 이동
    @GetMapping("/create")
    public String showCreatePostForm(Model model) {
        model.addAttribute("post", new Post()); // 새로운 빈 Post 객체를 모델에 추가
        return "create"; // create.html로 이동
    }
    @PostMapping("/create")
    public String createPost(@ModelAttribute Post post, @RequestParam("image") MultipartFile imageFile, RedirectAttributes redirectAttributes,@RequestParam Double latitude,
                             @RequestParam Double longitude) {
        try {
            // 이미지 파일 저장
            if (!imageFile.isEmpty()) {
                String uploadDir = "src/main/resources/static/images/";
                String imagePath = uploadDir + imageFile.getOriginalFilename();
                Path path = Paths.get(imagePath);
                Files.copy(imageFile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                post.setImagePath("/images/" + imageFile.getOriginalFilename()); // 저장 경로를 데이터베이스에 저장
                post.setLatitude(latitude); // 위도 저장
                post.setLongitude(longitude); // 경도 저장
            }

            // 게시물 저장
            postService.createPost(post);
            redirectAttributes.addFlashAttribute("message", "Post created successfully!");

        } catch (IOException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("message", "Failed to upload image!");
        }

        return "redirect:/posts"; // 게시물 생성 후 목록으로 리다이렉트
    }


    @GetMapping("/delete/{id}")
    public String deletePost(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        postService.deletePost(id);
        redirectAttributes.addFlashAttribute("message", "Post deleted successfully!");
        return "redirect:/posts"; // 게시물 삭제 후 목록으로 리다이렉트
    }

    // 게시물 상세 페이지 표시
    @GetMapping("/{id}")
    public String viewPostDetail(@PathVariable Long id, Model model) {
        Optional<Post> post = postService.getPostById(id);
        if (post.isPresent()) {
            model.addAttribute("post", post.get());
            return "detail"; // post-detail.html로 이동 (상세 페이지)
        } else {
            return "redirect:/posts"; // 게시물이 없는 경우 목록으로 리다이렉트
        }
    }

    // 게시물 수정 페이지 표시
    @GetMapping("/edit/{id}")
    public String editPost(@PathVariable Long id, Model model) {
        Post post = postService.getPostById(id).orElseThrow(() -> new RuntimeException("Post not found"));
        model.addAttribute("post", post);
        return "edit"; // edit.html로 이동
    }

    @PostMapping("/edit")
    public String updatePost(@ModelAttribute Post post, @RequestParam("image") MultipartFile file) throws IOException {
        // 게시물 업데이트 처리
        if (!file.isEmpty()) {
            // 새로운 이미지가 업로드된 경우
            String imagePath = saveUploadedFile(file); // 파일 저장 메서드 호출
            post.setImagePath(imagePath); // 새로운 이미지 경로 설정
        }

        postService.createPost(post); // 같은 메서드를 사용하여 업데이트
        return "redirect:/posts"; // 업데이트 후 목록으로 리다이렉트
    }

    // 파일 저장 메서드
    private String saveUploadedFile(MultipartFile file) throws IOException {
        // 원하는 경로에 파일 저장 로직 구현
        String uploadDir = "src/main/resources/static/images/"; // 예시 경로
        Path path = Paths.get(uploadDir + file.getOriginalFilename());
        Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        return "/images/" + file.getOriginalFilename(); // 적절한 이미지 경로 반환
    }
}
