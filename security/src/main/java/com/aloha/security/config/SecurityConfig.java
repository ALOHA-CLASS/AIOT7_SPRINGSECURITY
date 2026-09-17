package com.aloha.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j 
@Configuration 
@EnableWebSecurity      // ⭐ 스프링 시큐리티 설정 빈으로 등록
@RequiredArgsConstructor 
public class SecurityConfig {

  private final PasswordEncoder passwordEncoder;
  
  /**
   * 스프링 시큐리티 설정
   * @param http
   * @return
   * @throws Exception
   */
  @Bean 
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    log.info("스프링 시큐리티 설정");

    // ✅ 인가 설정
    http.authorizeHttpRequests( auth -> auth
            .requestMatchers("/admin", "/admin/**").hasRole("ADMIN")  // 관리자 권한인 경우 허용
            .requestMatchers("/**").permitAll()                       // 전체 허용
            );
    
    // 🔐 폼 로그인 설정
    http.formLogin(login -> login.permitAll());


    return http.build();
  }


  // 인메모리 방식 인증
  @Bean
  public UserDetailsService userDetailsService() {
      UserDetails admin = User.builder()
              .username("admin") // 사용자 이름
              // .password("{noop}123456") // 비밀번호 (noop: 평문 처리)
              .password( passwordEncoder.encode("123456") )
              .roles("ADMIN") // ROLE_ADMIN 권한
              .build();

      UserDetails user = User.builder()
              .username("user")
              // .password("{noop}123456")
              .password( passwordEncoder.encode("123456") )
              .roles("USER") // ROLE_USER 권한
              .build();
      // 인메모리 방식 인증
      return new InMemoryUserDetailsManager(admin, user);
  }

  
  
}
