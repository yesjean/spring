package com.example.demo.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.core.Authentication;

import javax.sql.DataSource;
import java.io.IOException;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final DataSource dataSource;

    public SecurityConfig(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf((csrf) -> csrf.ignoringRequestMatchers("/posts/sendEmail"))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/static/**", "/images/**", "/css/**", "/js/**","/files/**", "/posts/sendEmail", "/Users/*").permitAll()
                        .requestMatchers("/admin").hasRole("ADMIN") // 관리자 권한 필요
                        .requestMatchers("/register").permitAll()  // antMatchers 대신 requestMatchers 사용
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form

                        .loginPage("/login")
                        .successHandler((request, response, authentication) -> {
                            // 로그인 성공 후 처리
                            System.out.println(authentication);
                            String role = authentication.getAuthorities().stream()
                                    .findFirst()
                                    .get()
                                    .getAuthority();

                            if (role.equals("ROLE_ADMIN")) {
                                response.sendRedirect("/admin"); // 관리자 페이지로 리다이렉트
                            } else {
                                response.sendRedirect("/posts"); // 일반 사용자 페이지로 리다이렉트
                            }
                        })
                        .permitAll()
//                        .defaultSuccessUrl("/posts", true)
                        .failureUrl("/login?error=true") // 실패 시 URL
                        .usernameParameter("username")
                        .passwordParameter("password")
                )
                .logout(logout -> logout
                        .logoutUrl("/logout") // 로그아웃 URL 설정
                        .logoutSuccessUrl("/login?logout")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .permitAll()
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Primary // 여기에서 Primary 어노테이션 추가
    public AuthenticationManagerBuilder configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        System.out.println(auth);
        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .usersByUsernameQuery("SELECT username, password, enabled FROM users WHERE username=?")
                .authoritiesByUsernameQuery("SELECT username, role FROM user_roles WHERE username=?")
                .passwordEncoder(passwordEncoder());
        return auth; // AuthenticationManagerBuilder 객체를 반환
    }

    @Bean
    public AuthenticationSuccessHandler successHandler() {
        return new AuthenticationSuccessHandler() {
            @Override
            public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                response.sendRedirect("/posts"); // 로그인 성공 시 posts 페이지로 리다이렉트
            }
        };
    }
}
