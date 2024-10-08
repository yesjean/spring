package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ProfileController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/profile")
    public String getProfilePage(Model model) {
        // 현재 인증된 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName(); // 현재 로그인된 사용자의 username

        // username으로 User 엔티티 가져오기
        User user = userService.findUserByUsername(currentUsername);

        model.addAttribute("user", user);
        return "profile"; // profile.html 템플릿으로 이동
    }

    @GetMapping("/profile/{userId}")
    public String getProfilePage(@PathVariable Long userId, Model model) {
        User user = userService.findUserById(userId); // userId로 사용자 찾기
        System.out.println(user);
        model.addAttribute("user", user);
        return "profile"; // profile.html 템플릿으로 이동
    }

    @PostMapping("/profile/update")
    public String updateUserProfile(@ModelAttribute User user, RedirectAttributes redirectAttributes) {
        // 기존의 사용자 정보를 불러와서 업데이트
        User existingUser = userRepository.findById(user.getId()).orElse(null);
        System.out.println(existingUser);
        if (existingUser != null) {
            existingUser.setName(user.getName());
            existingUser.setEmail(user.getEmail());
            existingUser.setLastName(user.getLastName());
            existingUser.setRole(user.getRole());
            existingUser.setEnabled(user.isEnabled());

            // 변경된 사용자 정보 저장
            userRepository.save(existingUser);

            // 성공 메시지 추가
            redirectAttributes.addFlashAttribute("message", "프로필이 성공적으로 업데이트되었습니다.");
        } else {
            // 사용자 ID가 없는 경우 오류 처리
            redirectAttributes.addFlashAttribute("error", "사용자를 찾을 수 없습니다.");
        }

        return "redirect:/profile"; // 저장 후 다시 프로필 페이지로 리다이렉트
    }

}
