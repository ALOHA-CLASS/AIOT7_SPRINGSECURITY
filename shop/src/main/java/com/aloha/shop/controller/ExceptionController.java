package com.aloha.shop.controller;

import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * 📌 전역 컨트롤러 클래스
 *
 * ■ @ControllerAdvice 란?
 *   * 모든 Controller 에 공통 적용되는 기능을 처리하는 클래스를 지정하는 어노테이션
 *   - 여기서는, 여러 컨트롤러에서 공통으로 발생하는 예외를 "한 곳에 모아서" 처리할 수 있게 해주는 애노테이션.
 *   - 만약 이게 없다면, 컨트롤러마다 try-catch 를 반복해서 작성해야 한다.
 *   - 쉽게 말해 "이 앱 전체(or 지정한 범위)에서 예외가 터지면 여기로 모여라" 라고 등록하는 것.
 *   - 비슷한 애노테이션으로 @RestControllerAdvice 가 있는데, 그건 JSON 응답(REST API)용이고,
 *     여기서는 HTML 화면(View 이름)을 반환해야 하므로 @ControllerAdvice 를 사용한다.
 *
 * ■ basePackages 옵션은 왜 썼나?
 *   - 이 프로젝트에는 화면을 반환하는 controller 패키지와,
 *     JSON 을 반환하는 api 패키지(CartApi, OrderApi 등)가 따로 있다.
 *   - basePackages = "com.aloha.shop.controller" 로 지정하면
 *     "이 패키지 안에 있는 컨트롤러에서 발생한 예외만" 이 클래스가 처리하게 된다.
 *   - 덕분에 api 패키지(REST API)에서 발생한 예외는 여기서 건드리지 않고
 *     스프링 부트 기본 JSON 에러 응답을 그대로 사용하게 된다. (화면 HTML과 API JSON을 분리)
 *
 * ■ @ExceptionHandler 가 여러 개 있을 때 우선순위는?
 *   - 스프링은 "더 구체적인(상속 관계상 더 자식에 가까운) 예외 타입"을 우선 매칭한다.
 *   - 예) ResponseStatusException 과 Exception 이 둘 다 핸들러로 등록되어 있다면,
 *     ResponseStatusException 이 터졌을 때는 더 구체적인
 *     responseStatusException() 메서드가 선택되고, exception() 은 호출되지 않는다.
 *     (선언 순서는 상관없다. 타입 상속관계로 우선순위가 정해진다.)
 */
@Slf4j // log.info(), log.warn(), log.error() 등을 바로 쓸 수 있게 해주는 롬복(Lombok) 애노테이션
@ControllerAdvice(basePackages = "com.aloha.shop.controller")
public class ExceptionController {

  // templates/error 폴더에 "전용 페이지"가 준비되어 있는 상태코드 목록.
  // 여기 없는 코드(예: 401 X, 415 등)는 아래 resolveErrorView() 에서
  // error/4xx.html 또는 error/5xx.html 공통 페이지로 대신 보여준다.
  private static final Set<Integer> KNOWN_STATUS = Set.of(400, 401, 403, 404, 500, 503);

  /**
   * 404 전용 처리
   *
   * - NoResourceFoundException : 존재하지 않는 정적 자원(css/js/img 등) 요청 시 발생
   * - NoHandlerFoundException  : 어떤 컨트롤러에도 매핑되지 않은 URL 요청 시 발생
   *   (단, NoHandlerFoundException 이 실제로 발생하려면
   *    application.properties 에 spring.mvc.throw-exception-if-no-handler-found=true,
   *    spring.web.resources.add-mappings=false 설정이 필요하다.
   *    설정이 없으면 보통 NoResourceFoundException 쪽으로 처리된다.)
   *
   * @ResponseStatus(HttpStatus.NOT_FOUND)
   *   - 이 메서드가 반환하는 뷰(error/404)와 별개로,
   *     "HTTP 응답 상태코드"를 404 로 고정해서 내려주는 역할을 한다.
   *   - 즉, 화면은 error/404.html 을 보여주면서 상태코드도 진짜 404 로 응답하게 된다.
   */
  @ExceptionHandler({ NoResourceFoundException.class, NoHandlerFoundException.class })
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public String notFound(HttpServletRequest request) {
    log.warn("404 Not Found : {}", request.getRequestURI());
    return "error/404"; // templates/error/404.html 로 포워딩
  }

  /**
   * 컨트롤러(또는 서비스)에서 throw new ResponseStatusException(상태코드, 사유) 로
   * "의도적으로" 발생시킨 예외를 처리한다. (예: ErrorController 의 테스트 엔드포인트들)
   *
   * - @ResponseStatus 를 메서드에 고정으로 붙이지 않은 이유:
   *   ResponseStatusException 은 400/401/403/500/503 등 "상태코드가 상황마다 다르기" 때문에
   *   고정된 @ResponseStatus 로는 대응할 수 없다.
   *   그래서 response.setStatus(status) 로 "실제 예외가 가진 상태코드"를
   *   직접 꺼내서 동적으로 설정해준다.
   *
   * - Model model 파라미터:
   *   Thymeleaf 템플릿(error/4xx.html, error/5xx.html)에서 ${status} 로 꺼내 쓸 수 있도록
   *   상태코드 값을 모델에 담아 화면까지 전달해준다.
   */
  @ExceptionHandler(ResponseStatusException.class)
  public String responseStatusException(ResponseStatusException e, HttpServletRequest request,
      HttpServletResponse response, Model model) {
    int status = e.getStatusCode().value();     // 예외가 들고 있는 실제 상태코드 (400, 401, 403 ...)
    response.setStatus(status);                 // 응답 상태코드를 해당 값으로 직접 세팅
    model.addAttribute("status", status);       // 화면(error/4xx, 5xx)에서 ${status} 로 사용
    log.warn("{} {} : {}", status, request.getRequestURI(), e.getReason());
    e.printStackTrace();
    return resolveErrorView(status);            // 상태코드에 맞는 뷰 이름 결정
  }

  /**
   * 위에서 처리하지 못한 "그 외 모든 예외"의 최종 처리 담당 (안전망 역할).
   * @ExceptionHandler(Exception.class) 는 가장 상위 타입이라
   * 다른 더 구체적인 핸들러가 없을 때만 호출된다.
   */
  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public String exception(Exception e, HttpServletRequest request) throws Exception {
    // 스프링 시큐리티 관련 예외(AccessDeniedException: 403, AuthenticationException: 로그인 필요)는
    // 여기서 "error/500" 으로 덮어버리면 안 된다.
    // 이 예외들은 시큐리티의 ExceptionTranslationFilter 가 받아서
    // 403 처리 또는 로그인 페이지 리다이렉트를 수행해야 하므로,
    // 다시 throw 해서 시큐리티 필터 체인으로 돌려보낸다.
    if (e instanceof AccessDeniedException || e instanceof AuthenticationException) {
      throw e;
    }
    log.error("500 Server Error : {}", request.getRequestURI(), e);
    e.printStackTrace();
    return "error/500";
  }

  /**
   * 상태코드별로 보여줄 뷰(템플릿) 이름을 결정하는 헬퍼 메서드.
   * - KNOWN_STATUS 에 있는 코드(400/401/403/404/500/503)는 전용 페이지(error/상태코드)로.
   * - 그 외 코드는 4xx/5xx 대역에 맞는 공통 페이지로 대체해서,
   *   "새로운 상태코드가 생겨도 에러 페이지가 아예 없어서 깨지는 일"을 방지한다.
   */
  private String resolveErrorView(int status) {
    if (KNOWN_STATUS.contains(status)) {
      return "error/" + status;
    }
    return status >= 500 ? "error/5xx" : "error/4xx";
  }
}