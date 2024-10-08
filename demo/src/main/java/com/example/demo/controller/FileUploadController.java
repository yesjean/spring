package com.example.demo.controller;

import java.io.IOException;
import java.util.stream.Collectors;

import com.example.demo.entity.Post;
import com.example.demo.repository.PostRepository;
import com.example.demo.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.uploadingfiles.storage.StorageFileNotFoundException;
import com.example.demo.uploadingfiles.storage.StorageService;

@Controller
public class FileUploadController {

    private final StorageService storageService;
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostService postService;

    @Autowired
    public FileUploadController(StorageService storageService) {
        this.storageService = storageService;
    }

    @GetMapping("/posts/upload")
    public String listUploadedFiles(Model model) throws IOException {
System.out.println(model);
        model.addAttribute("files", storageService.loadAll().map(
                        path -> MvcUriComponentsBuilder.fromMethodName(FileUploadController.class,
                                "serveFile", path.getFileName().toString()).build().toUri().toString())
                .collect(Collectors.toList()));

        return "redirect:/posts";
    }

    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

        Resource file = storageService.loadAsResource(filename);
        System.out.println(file.getFilename());
        if (file == null)
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @PostMapping("/posts/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file,
                                   @RequestParam("title") String title, // 제목 추가
                                   @RequestParam("content") String content, // 내용 추가
                                   RedirectAttributes redirectAttributes,
                                   Model model,
                                   @ModelAttribute Post post) {
        System.out.println("Uploaded file name: " + file.getOriginalFilename());
        System.out.println("File size: " + file.getSize());

//        storageService.store(file);
        String filePath = storageService.store(file);

        // 데이터베이스에 경로 저장
        post.setImagePath(filePath); // 저장된 파일의 경로를 설정
        post.setTitle(title); // 제목 설정
        post.setContent(content); // 내용 설정

        postRepository.save(post); // 데이터베이스에 저장
        postService.createPost(post);
        postRepository.save(post); // 데이터베이스에 저장
        redirectAttributes.addFlashAttribute("message",
                "You successfully uploaded " + file.getOriginalFilename() + "!");
        model.addAttribute( postRepository.findAll());
        return "redirect:/posts";
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }
    @ExceptionHandler(MultipartException.class)
    public ResponseEntity<String> handleMultipartException(MultipartException e) {
        return new ResponseEntity<>("파일 업로드 중 오류가 발생했습니다: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}