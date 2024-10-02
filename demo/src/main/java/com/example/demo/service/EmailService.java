package com.example.demo.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
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

    public void sendEmail(String to, String title, String content, String imagePath) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        try {
            helper.setTo(to);
            helper.setSubject(title);
            helper.setText(content, true);
            FileSystemResource file2 = new FileSystemResource(new File("src/main/resources/static/"+imagePath));
        System.out.println(file2.getPath());
            // 첨부 파일 추가
            File file = new File(file2.getPath());
            if (file.exists()) {
                helper.addAttachment(file.getName(), file);
            } else {
                throw new FileNotFoundException("File not found: " + imagePath);
            }

        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
            throw new MessagingException("Error sending email: " + e.getMessage(), e);
        }

        mailSender.send(message);
    }

}
