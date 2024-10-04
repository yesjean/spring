package com.example.demo.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Autowired
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendEmailWithPostImage(String recipient, String subject, String content, String imagePath) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(recipient);
            helper.setSubject(subject);
            helper.setText(
                    "<html><body>" +
                            "<p>" + content + "</p>" +
                            "<img src='" + imagePath + "' alt='Post Image' style='width:300px;'/>" +
                            "</body></html>",
                    true
            );

            // 이미지 파일 첨부
            FileSystemResource file = new FileSystemResource(new File("src/main/resources/static/" + imagePath));
            if (file.exists()) {
                helper.addAttachment(file.getFilename(), file);
            } else {
                throw new FileNotFoundException("File not found: " + imagePath);
            }

            mailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
