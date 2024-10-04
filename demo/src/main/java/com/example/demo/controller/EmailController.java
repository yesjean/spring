package com.example.demo.controller;

import com.example.demo.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController // @Controller 대신 @RestController 사용
public class EmailController {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private EmailService emailService;

    @PostMapping("/posts/sendEmail")
    public ResponseEntity<?> sendEmail(@RequestBody EmailRequest emailRequest) {
        try {
            String recipient = emailRequest.getRecipient();
            String subject = emailRequest.getSubject();
            String message = emailRequest.getMessage();
            String imagePath = emailRequest.getImagePath(); // 이미지 경로 추가

            // 이메일 전송 로직
            emailService.sendEmailWithPostImage(recipient, subject, message, imagePath);
            return ResponseEntity.ok("메일 전송 완료!");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("메일 전송 실패!");
        }
    }
}
