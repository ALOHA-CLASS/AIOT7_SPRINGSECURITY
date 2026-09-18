package com.aloha.security.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import com.aloha.security.service.UserDetailServiceImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j 
@Configuration 
@EnableWebSecurity      // ⭐ 스프링 시큐리티 설정 빈으로 등록
@RequiredArgsConstructor 
public class SecurityConfig {

  private final PasswordEncoder passwordEncoder;
  private final DataSource dataSource;
  private final UserDetailServiceImpl userDetailServiceImpl;
  
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
    http.formLogin(login -> login
      .loginPage("/login")                // 커스텀 로그인 페이지 경로
      .loginProcessingUrl("/login")       // 로그인 처리 요청 경로
      .defaultSuccessUrl("/?login=true")  // 로그인 성공 시 이동할 경로
      .failureUrl("/login?error=true")    // 로그인 실패 시 이동할 경로
    );

    // 🔄 자동 로그인 설정
    http.rememberMe(me -> me
                        .key("aloha") 
                        .tokenRepository(tokenRepository())       // 자동 로그인 저장소 빈 지정
                        .tokenValiditySeconds(60 * 60 * 24 * 7)   // 자동 로그인 토큰 유효 기간(7일)
    );

    // 👩‍💼 사용자 정의 인증
    http.userDetailsService(userDetailServiceImpl);


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
  // @Bean
  // public UserDetailsService userDetailsService() {
  //   // 데이터 소스 설정
  //   JdbcUserDetailsManager userDetailsManager = new JdbcUserDetailsManager(dataSource);

  //   // 회원 인증 쿼리 설정
  //   String sql1 = " SELECT username, password, enabled "
  //               + " FROM user "
  //               + " WHERE username = ? ";

  //   // 회원 권한 쿼리 설정
  //   String sql2 = " SELECT u.username, ua.auth "
  //               + " FROM user u "
  //               + " LEFT JOIN user_auth ua ON u.no = ua.user_no "
  //               + " WHERE u.username = ? "
  //               ;
               

  //   userDetailsManager.setUsersByUsernameQuery(sql1);
  //   userDetailsManager.setAuthoritiesByUsernameQuery(sql2);
  //   return userDetailsManager;
  // }
  

  /**
    * 🍃 자동 로그인 저장소 빈 등록
    * ✅ 데이터 소스
    * ⭐ persistent_logins 테이블 생성
          create table persistent_logins (
              username varchar(64) not null
              , series varchar(64) primary key
              , token varchar(64) not null
              , last_used timestamp not null
          );
    * 🔄 자동 로그인 프로세스
    * ✅ 로그인 시 
    *     ➡ 👩‍💼(ID, 시리즈, 토큰) 저장
    * ✅ 로그아웃 시, 
    *     ➡ 👩‍💼(ID, 시리즈, 토큰) 삭제
    * @return
  */
  @Bean 
  public PersistentTokenRepository tokenRepository() {
    // JdbcTokenRepositoryImpl : 토큰 저장 데이터 베이스를 등록하는 객체
    JdbcTokenRepositoryImpl repositoryImpl = new JdbcTokenRepositoryImpl();
    // ✅ 토큰 저장소를 사용하는 데이터 소스 지정
    // - SpringSecurity 가 자동 로그인 프로세스를 처리하기 위한 DB를 지정합니다.
    repositoryImpl.setDataSource(dataSource);

    // persistent_logins 테이블 생성
    try {
      repositoryImpl.getJdbcTemplate().execute(JdbcTokenRepositoryImpl.CREATE_TABLE_SQL);
    } catch (BadSqlGrammarException e) {
      log.error("persistent_logins 테이블이 이미 존재합니다.");
    } catch (Exception e) {
      log.error("자동 로그인 테이블 생성 중, 예외 발생");
    }
    return repositoryImpl;
  }


  /**
   * 🍃 AuthenticationManager 인증 관리자 빈 등록
   * @param authenticationConfiguration
   * @return
   * @throws Exception
  */
  @Bean 
  public AuthenticationManager authenticationManager(
    AuthenticationConfiguration authenticationConfiguration
  ) throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
  }
  
  
}
