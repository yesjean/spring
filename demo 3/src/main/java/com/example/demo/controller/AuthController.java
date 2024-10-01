package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register"; // register.html로 이동
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user) {
        userService.registerUser(user);
        return "redirect:/login"; // 로그인 페이지로 리다이렉트
    }

    @GetMapping("/login")
    public String showLoginForm() {
        System.out.println("로그인...");
        return "login"; // login.html로 이동
    }

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password, Model model) {
        String role = userService.authenticate(username, password); // 사용자 역할을 가져옵니다.
        System.out.println(role);

        if (role != null) {
            if (role.equals("ROLE_ADMIN")) {
                return "redirect:/admin/dashboard"; // 관리자 페이지로 리다이렉트
            } else {
                return "redirect:/posts"; // 일반 사용자 페이지로 리다이렉트
            }
        } else {
            model.addAttribute("error", "잘못된 사용자 이름 또는 비밀번호입니다.");
            return "login"; // 로그인 실패 시 로그인 페이지로 돌아감
        }
    }



}
