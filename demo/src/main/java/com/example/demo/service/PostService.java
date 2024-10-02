package com.example.demo.service;

import com.example.demo.entity.Post;
import com.example.demo.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public Optional<Post> getPostById(Long id) {
        return postRepository.findById(id);
    }

    public Post createPost(Post post) {
        System.out.println(post);
        return postRepository.save(post);
    }

    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }

    public void savePostWithImage(String title, String content, String imageName) {
        Post post = new Post();
        post.setTitle(title);
        post.setContent(content);
        post.setImagePath("/uploads/" + imageName); // 이미지 경로 저장
        System.out.println(imageName);
        postRepository.save(post);
    }

    public void savePost(Post post, MultipartFile file) {
        if (!file.isEmpty()) {
            String fileName = file.getOriginalFilename();
            String filePath = "path/to/save/" + fileName; // 실제 저장 경로로 변경
            try {
                file.transferTo(new File(filePath));
                post.setImagePath(filePath); // 이미지 경로 설정
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        postRepository.save(post); // 게시물 저장
    }
}
