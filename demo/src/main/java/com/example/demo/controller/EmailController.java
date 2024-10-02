package com.example.demo.controller;

import com.example.demo.service.EmailService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class EmailController {

    @Autowired
    private JavaMailSender mailSender;


    @Autowired
    private EmailService emailService;

    @PostMapping("/posts/sendEmail")
    public String sendEmail(@RequestParam String to, @RequestParam String title, @RequestParam String content,  @RequestParam String imagePath) throws MessagingException {
        SimpleMailMessage message = new SimpleMailMessage();
//        message.setTo(to);
//        message.setSubject(title);
//        message.setText(content);
        emailService.sendEmail(to, title, content,imagePath);
        return "redirect:/posts"; // 이메일 전송 후 리다이렉트할 페이지
    }

//    @PostMapping("/posts/sendEmail")
//    public String sendEmail(@RequestParam String to, @RequestParam String title, @RequestParam String content) {
//        emailService.sendEmail(to, title, content);
//        return "redirect:/posts"; // 이메일 전송 후 리다이렉트할 페이지
//    }
}

