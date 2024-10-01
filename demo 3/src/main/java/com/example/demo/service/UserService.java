package com.example.demo.service;

import com.example.demo.entity.User;
import com.example.demo.entity.UserRole;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.UserRoleRepository;
import org.antlr.v4.runtime.misc.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    private final UserRoleRepository userRoleRepository; // 추가
    private PasswordEncoder password;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserService(UserRoleRepository userRoleRepository) {
        this.userRoleRepository = userRoleRepository;
    }

    public void registerUser(User user) {
        System.out.println("Saving user: " + user.getUsername());
        System.out.println("비밀번호: " + user.getPassword());
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
    public boolean authenticate(String username, String password) {
        System.out.println("로그인 되니?");
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isPresent()) {
            System.out.println("로그인 됨?");

            User user = userOptional.get();
            // 입력된 비밀번호와 저장된 비밀번호를 비교
            return passwordEncoder.matches(password, user.getPassword());
        }
        return false; // 사용자 이름이 존재하지 않는 경우
    }
}
