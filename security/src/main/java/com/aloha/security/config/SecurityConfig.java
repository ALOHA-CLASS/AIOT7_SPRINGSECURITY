package com.aloha.security.config;

import javax.sql.DataSource;

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
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j 
@Configuration 
@EnableWebSecurity      // ⭐ 스프링 시큐리티 설정 빈으로 등록
@RequiredArgsConstructor 
public class SecurityConfig {

  private final PasswordEncoder passwordEncoder;
  private final DataSource dataSource;
  
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
            // requestMatchers("경로") : 접근 허용 범위 지정할 경로를 설정
            // hasRole("권한")         : 해당 경로에 대해서, 접근 가능한 권한을 설정
            // permitAll()             : 해당 경로에 모든 접근 허용
            .requestMatchers("/admin", "/admin/**").hasRole("ADMIN")  // 관리자 권한인 경우 허용
            .requestMatchers("/**").permitAll()                       // 전체 허용
            );
    
    // 🔐 폼 로그인 설정
    http.formLogin(login -> login.permitAll());


    return http.build();
  }


  // * UserDetailsService : 사용자 정보를 불러오는 인터페이스
  // - 빈으로 등록하여 인증 방식을 설정할 수 있다.
  // 1. 인메모리 인증 방식
  // 2. JDBC 인증 방식
  // 3. 사용자 정의 인증 방식

  // ⚡ 인메모리 방식 인증
  // @Bean
  // public UserDetailsService userDetailsService() {
  //     UserDetails admin = User.builder()
  //             .username("admin") // 사용자 이름
  //             // .password("{noop}123456") // 비밀번호 (noop: 평문 처리)
  //             .password( passwordEncoder.encode("123456") )
  //             .roles("ADMIN") // ROLE_ADMIN 권한
  //             .build();

  //     UserDetails user = User.builder()
  //             .username("user")
  //             // .password("{noop}123456")
  //             .password( passwordEncoder.encode("123456") )
  //             .roles("USER") // ROLE_USER 권한
  //             .build();
  //     // 인메모리 방식 인증
  //     return new InMemoryUserDetailsManager(admin, user);
  // }

  // ⚡ JDBC 인증 방식
  // 1. 데이터 소스 (호스트, 아이디, 비밀번호) 설정
  // 2. 회원 인증 쿼리 설정
  // 3. 회원 권한 쿼리 설정
  @Bean
  public UserDetailsService userDetailsService() {
    // 데이터 소스 설정
    JdbcUserDetailsManager userDetailsManager = new JdbcUserDetailsManager(dataSource);

    // 회원 인증 쿼리 설정
    String sql1 = " SELECT username, password, enabled "
                + " FROM user "
                + " WHERE username = ? ";

    // 회원 권한 쿼리 설정
    String sql2 = " SELECT username, auth "
                + " FROM user_auth "
                + " WHERE username = ? ";

    userDetailsManager.setUsersByUsernameQuery(sql1);
    userDetailsManager.setAuthoritiesByUsernameQuery(sql2);
    return userDetailsManager;
  }
  
  
}
