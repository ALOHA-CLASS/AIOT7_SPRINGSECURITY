package com.aloha.shop.controller;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.server.ResponseStatusException;

/**
 * 📌 에러 페이지 "수동 테스트"용 컨트롤러
 *
 * 실제 서비스에서는 400/401/403/404/500/503 같은 에러가
 * "의도치 않게" 발생하기 때문에 평소에는 화면을 직접 보기 어렵다.
 * 그래서 아래처럼 일부러 예외를 던지는 테스트용 경로를 만들어두면
 * 브라우저에서 /error/400, /error/404 ... 로 직접 접속해서
 * templates/error 밑의 에러 페이지들이 의도한 대로 잘 나오는지 눈으로 확인할 수 있다.
 *
 * - 핵심 아이디어
 *   1) 여기서는 {@link ResponseStatusException} 을 직접 던지기만 한다.
 *      (= "나 이런 상태코드의 에러가 났어!" 라고 스프링에게 알려주는 예외)
 *   2) 이 예외를 실제로 받아서 "어떤 화면을 보여줄지" 결정하는 쪽은
 *      이 클래스가 아니라 {@link ExceptionController} 이다.
 *      (= @ExceptionHandler(ResponseStatusException.class) 가 처리)
 *   3) 즉, 이 클래스는 "에러 발생 담당", ExceptionController 는 "에러 처리 담당"
 *      으로 역할이 분리되어 있다.
 *
 * - 실제 서비스 코드에서는 이렇게 테스트용 엔드포인트를 만들지 않고,
 *   각 도메인 로직(예: 상품 조회, 주문 처리)에서 조건에 맞지 않으면
 *   아래와 똑같은 방식으로 ResponseStatusException 을 던지면 된다.
 *   예) 존재하지 않는 상품 조회 시
 *       throw new ResponseStatusException(HttpStatus.NOT_FOUND, "상품을 찾을 수 없습니다");
 */
@Controller
public class ErrorController {

  // --------------------------------------------------------------------
  // 400 Bad Request : 요청 자체가 잘못된 경우 (필수 파라미터 누락, 형식 오류 등)
  // --------------------------------------------------------------------
  @GetMapping("/error/400")
  public String error400() {
    // ResponseStatusException(상태코드, 사유) 를 던지면
    // 스프링이 응답 상태코드를 400 으로 세팅하고,
    // ExceptionController 가 이를 가로채서 error/400.html 로 연결해준다.
    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "잘못된 요청 테스트");
  }

  // --------------------------------------------------------------------
  // 401 Unauthorized : 로그인(인증) 자체가 안 된 경우
  // (참고) 실제 서비스에서는 보통 스프링 시큐리티가 비로그인 접근을
  //        로그인 페이지로 "리다이렉트" 시키기 때문에 401 화면을 직접 볼 일은 드물다.
  //        여기서는 "401 페이지 자체"가 정상 출력되는지만 확인하는 용도.
  // --------------------------------------------------------------------
  @GetMapping("/error/401")
  public String error401() {
    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "인증 필요 테스트");
  }

  // --------------------------------------------------------------------
  // 403 Forbidden : 로그인은 했지만 권한이 없는 경우
  // (참고) 실제로는 스프링 시큐리티의 AccessDeniedException 이 주로 403 을 발생시키는데,
  //        그 예외는 ExceptionController 에서 다시 던져서(throw) 시큐리티가 처리하도록
  //        위임하고 있다. 여기서는 그냥 "403 페이지"가 잘 보이는지만 확인.
  // --------------------------------------------------------------------
  @GetMapping("/error/403")
  public String error403() {
    throw new ResponseStatusException(HttpStatus.FORBIDDEN, "접근 권한 없음 테스트");
  }

  // --------------------------------------------------------------------
  // 404 Not Found : 요청한 자원(페이지, 데이터)이 존재하지 않는 경우
  // (참고) 실제 "존재하지 않는 URL" 로 접속했을 때의 진짜 404 는
  //        ExceptionController#notFound(NoResourceFoundException 등) 가 처리한다.
  //        여기 /error/404 는 그것과 별개로, 도메인 로직에서
  //        "데이터를 못 찾았을 때" 404 를 던지는 상황을 흉내낸 것이다.
  // --------------------------------------------------------------------
  @GetMapping("/error/404")
  public String error404() {
    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "페이지 없음 테스트");
  }

  // --------------------------------------------------------------------
  // 415 Unsupported Media Type : 서버가 처리할 수 없는 요청 본문 형식
  // (참고) templates/error 에는 415 전용 페이지가 없다.
  //        이 경우 ExceptionController#resolveErrorView() 가
  //        4xx 공통 페이지(error/4xx.html)로 대체해서 보여준다.
  //        → "전용 페이지가 없는 상태코드도 안전하게 처리되는지" 확인하는 테스트.
  // --------------------------------------------------------------------
  @GetMapping("/error/415")
  public String error415() {
    throw new ResponseStatusException(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "지원하지 않는 미디어 타입 테스트");
  }

  // --------------------------------------------------------------------
  // 500 Internal Server Error : 서버 내부 로직 처리 중 예기치 못한 오류
  // --------------------------------------------------------------------
  @GetMapping("/error/500")
  public String error500() {
    throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류 테스트");
  }

  // --------------------------------------------------------------------
  // 503 Service Unavailable : 서버 점검/과부하 등으로 일시적으로 응답 불가
  // --------------------------------------------------------------------
  @GetMapping("/error/503")
  public String error503() {
    throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "서비스 이용 불가 테스트");
  }

}
