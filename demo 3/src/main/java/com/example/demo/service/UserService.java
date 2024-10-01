package com.example.demo.service;

import com.example.demo.entity.Post;
import com.example.demo.entity.User;
import com.example.demo.entity.UserRole;
import com.example.demo.repository.PostRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.UserRoleRepository;
import org.antlr.v4.runtime.misc.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    private final UserRoleRepository userRoleRepository; // 추가
    private final PostRepository postRepository;
    private PasswordEncoder password;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserService(UserRoleRepository userRoleRepository, PostRepository postRepository) {
        this.userRoleRepository = userRoleRepository;
        this.postRepository = postRepository;
    }
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public List<Post> findAllPosts() {
        return postRepository.findAll();
    }
    public void registerUser(User user) {
        user.setRole("ROLE_USER"); // 기본 역할
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        try {
            userRepository.save(user);
            userRoleRepository.save(new UserRole(user.getUsername(), "ROLE_USER")); // UserRole은 user_roles에 매핑된 Entity 클래스입니다.
            System.out.println("사용자 정보가 저장되었습니다: " + user);
        } catch (Exception e) {
            System.out.println("사용자 저장 실패: " + e.getMessage());
        }
    }

    // 로그인 처리
    public String authenticate(String username, String password) {
        System.out.println("Username: " + username + ", Password: " + password);

        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            // 비밀번호 비교
            if (passwordEncoder.matches(password, user.getPassword())) {
                return user.getRole(); // 사용자 역할을 반환
            }
        }
        return null; // 사용자 이름이 존재하지 않거나 비밀번호가 일치하지 않음
    }
    // 사용자 삭제 메서드 추가
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }
}
