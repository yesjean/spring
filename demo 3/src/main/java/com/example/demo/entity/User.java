package com.example.demo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;

    private String password;

    private String name; // 사용자 이름 추가

    @Column(unique = true)
    private String email; // 사용자 이메일 추가

    private String emailId; // 추가적인 이메일 ID 추가

    private String lastName; // 사용자 성 추가
    private boolean enabled = true; // 사용자 활성화 상태
    // 기본 생성자
    public User() {}

    // Getter 및 Setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;  // 이 메서드가 필요합니다.
    }

    public void setPassword(String password) {
        this.password = password;  // 이 메서드가 필요합니다.
    }

    @Column(name = "role")
    private String role; // 사용자 역할

    public void setRole(String roleUser) {
        this.role = roleUser;
    }

    public String getRole() {
        return  this.role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
