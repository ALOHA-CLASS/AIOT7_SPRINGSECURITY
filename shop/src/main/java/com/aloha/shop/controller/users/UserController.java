package com.aloha.shop.controller.users;

import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.aloha.shop.domain.users.Address;
import com.aloha.shop.domain.users.User;
import com.aloha.shop.dto.users.AddressDto;
import com.aloha.shop.dto.users.PasswordChangeDto;
import com.aloha.shop.dto.users.PasswordCheckDto;
import com.aloha.shop.dto.users.UserJoinDto;
import com.aloha.shop.dto.users.UserUpdateDto;
import com.aloha.shop.security.CustomUser;
import com.aloha.shop.service.users.AddressService;
import com.aloha.shop.service.users.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;








@Slf4j 
@Controller 
@RequestMapping("/users")
@RequiredArgsConstructor 
public class UserController {
  
  private final UserService userService;
  private final AddressService addressService;

  // 비밀번호 확인 완료 세션 키
  private static final String PASSWORD_VERIFIED = "PASSWORD_VERIFIED";

  // 회원가입 화면
  // @ModelAttribute : 해당 파라미터를 모델로 자동 지정
  @GetMapping("/join")
  public String join(@ModelAttribute("joinDto") UserJoinDto joinDto) {

    // page/users/join.html 뷰 지정
    return "page/users/join";
  }

  // 회원가입 처리
  // - @Valid : 해당 객체의 유효성을 검증
  // - 
  @PostMapping("/join")
  public String join(@Valid @ModelAttribute("joinDto") UserJoinDto joinDto,
                    BindingResult bindingResult,
                    RedirectAttributes ra
  ) {
    // 유효성 검사 실패
    if(bindingResult.hasErrors()) {
      return "page/users/join";
    }
    // 아이디 중복 확인
    if( !userService.isUsernameAvailable(joinDto.getUsername()) ) {
      bindingResult.rejectValue("username", "duplicate", "이미 사용 중인 아이디입니다.");
      return "page/users/join";
    }
    userService.join(joinDto);
    ra.addFlashAttribute("message", "회원가입이 완료되었습니다. 로그인해 주세요.");
    return "redirect:/users/login";
  }

  // 로그인 화면
  @GetMapping("/login")
  public String login() {
    return "page/users/login";
  }

  // 마이페이지
  @GetMapping("/mypage")
  public String mypage(
    @AuthenticationPrincipal CustomUser loginUser, Model model
  ) {
    User user = loginUser.getUser();
    model.addAttribute("user", user);
    return "page/mypage/index";
  }
  
  // 회원 정보 수정 화면
  @GetMapping("/mypage/info")
  public String info(
    @AuthenticationPrincipal CustomUser loginUser, Model model
  ) {
    // 로그인된 회원 아이디로, 실시간으로 회원 정보 조회
    String username = loginUser.getUsername();
    User user = userService.select(username);
    UserUpdateDto dto = new UserUpdateDto();
    dto.setName(user.getName());
    dto.setEmail(user.getEmail());
    dto.setPhone(user.getPhone());
    model.addAttribute("updateDto", dto);
    model.addAttribute("user", user);

    return "page/mypage/info";
  }
  
  // 회원정보 수정 처리
  @PostMapping("/mypage/info")
  public String updateInfo(
    @AuthenticationPrincipal CustomUser loginUser,            // 로그인된 사용자 정보
    @Valid                                                    // 유효성 검사
    @ModelAttribute("updateDto") UserUpdateDto updateDto,     // "updateDto" 로 모델이 등록
    BindingResult bindingResult,                              // 유효성 검사 오류 결과
    Model model,                                              // View 데이터 전달하는 객체
    RedirectAttributes ra                                     // 리다이렉트 시 데이터 전달
  ) {
    String username = loginUser.getUsername();
    // 유효성 검사 확인
    if( bindingResult.hasErrors() ) {
      // 에러 발생하면, 다시 회원정보 수정 화면으로
      User user = userService.select(username);
      model.addAttribute("user", user);
      return "page/mypage/info";
    }

    // 회원 정보 수정
    User updatedUser = userService.update(username, updateDto);
    log.info("updatedUser : {}", updatedUser);

    // 수정된 사용자 정보를 인증 정보에 갱신
    CustomUser updatedLoginUser = new CustomUser(updatedUser);
    Authentication authentication = new UsernamePasswordAuthenticationToken(
                                                    updatedLoginUser, 
                                                    updatedLoginUser.getPassword(),
                                                    updatedLoginUser.getAuthorities()
                                                  );
    SecurityContextHolder.getContext().setAuthentication(authentication);                                          

    ra.addFlashAttribute("message", "회원정보가 수정되었습니다.");
    return "redirect:/users/mypage/info";
  }

  // 배송지 관리 화면
  @GetMapping("/mypage/address")
  public String address(
    @AuthenticationPrincipal CustomUser loginUser,            // 로그인된 사용자 정보
    Model model,
    @ModelAttribute("addressDto") AddressDto addressDto
  ) {
    // 회원의 배송지 목록 조회
    Long userNo = loginUser.getUser().getNo();
    List<Address> addressList = addressService.list(userNo);

    model.addAttribute("addressList", addressList);
    
    return "page/mypage/address";
  }
  

  // 배송지 추가
  @PostMapping("/mypage/address")
  public String addAddress(
    @AuthenticationPrincipal CustomUser loginUser,            // 로그인된 사용자 정보
    @Valid                                                    // 유효성 검사
    @ModelAttribute("addressDto") AddressDto addressDto,     // "updateDto" 로 모델이 등록
    BindingResult bindingResult,                              // 유효성 검사 오류 결과
    Model model,                                              // View 데이터 전달하는 객체
    RedirectAttributes ra                                     // 리다이렉트 시 데이터 전달
  ) {
    Long userNo = loginUser.getUser().getNo();

    // 배송지 유효성 
    if( bindingResult.hasErrors() ) {
      List<Address> addressList = addressService.list(userNo);
      model.addAttribute("addressList", addressList );
    }
    // 배송지 등록
    addressService.add(userNo, addressDto);
    ra.addFlashAttribute("message", "배송지가 추가되었습니다.");
    return "redirect:/users/mypage/address/add";
  }

  // 배송지 추가 화면
  @GetMapping("/mypage/address/add")
  public String addressAdd(
    @ModelAttribute("addressDto") AddressDto addressDto
  ) {
    return "page/mypage/address/add";
  }
  
  // 배송지 수정 화면
  @GetMapping("/mypage/address/update/{id}")
  public String addressUpdate(
    @PathVariable("id") String id,
    @ModelAttribute("addressDto") AddressDto addressDto,
    @AuthenticationPrincipal CustomUser loginUser,
    Model model
  ) {
    // id 로 배송지 정보 조회
    Long userNo = loginUser.getUser().getNo();
    Address address = addressService.select(userNo, id);
    
    // 모델에 등록
    model.addAttribute("address", address);

    // 뷰 지정
    return "page/mypage/address/update";
  }
  
  // 비밀번호 확인 화면
  @GetMapping("/mypage/password/check")
  public String passwordCheck(
    @ModelAttribute("checkDto") PasswordCheckDto checkDto,
    HttpSession session
  ) {
    // 확인 화면에 들어오면 비밀번호 확인 완료 정보를 제거
    session.removeAttribute(PASSWORD_VERIFIED);
    return "page/mypage/password-check";
  }
  
  // 비밀번호 확인 처리
  @PostMapping("/mypage/password/check")
  public String passwordCheck(
    @AuthenticationPrincipal CustomUser loginUser,
    @Valid @ModelAttribute("checkDto") PasswordCheckDto checkDto,
    BindingResult bindingResult,
    HttpSession session
  ) {
    // 비밀번호 미 입력 시
    if( bindingResult.hasErrors() ) {
      return "page/mypage/password-check";
    }
    // 비밀번호 일치 여부 확인
    String username = loginUser.getUser().getUsername();
    String passwrod = checkDto.getPassword();
    boolean result = userService.checkPassword(username, passwrod);

    // 불일치
    if( !result ) {
      bindingResult.rejectValue("password", "mismatch", "비밀번호가 일치하지 않습니다.");
      return "page/mypage/password-check";
    }
    // 현재 비밀번호 확인 완료 여부를 세션에 저장
    session.setAttribute(PASSWORD_VERIFIED, true);
    return "redirect:/users/mypage/password";
  }

  // 비밀번호 변경 화면
  @GetMapping("/mypage/password")
  public String password(
    @ModelAttribute("changeDto") PasswordChangeDto changeDto,
    HttpSession session
  ) {
    // 현재 비밀번호 일치 여부가 없으면
    if( session.getAttribute(PASSWORD_VERIFIED) == null ) {
      return "redirect:/users/mypage/password/check";
    }

    return "page/mypage/password";
  }

  // 비밀번호 변경처리
  @PostMapping("/mypage/password")
  public String changePassword(
    @AuthenticationPrincipal CustomUser loginUser,
    @Valid @ModelAttribute("changeDto") PasswordChangeDto changeDto,
    BindingResult bindingResult,
    HttpSession session,
    RedirectAttributes ra
  ) {
    // 유효성 검사
    // - 비밀번호 확인 완료 여부 체크
    if( session.getAttribute(PASSWORD_VERIFIED) == null ) {
      return "redirect:/users/mypage/password/check";
    }
    // 비밀번호 6자리 이상, 확인하고 일치 여부 체크
    if( !bindingResult.hasFieldErrors("newPassword")
        && !bindingResult.hasFieldErrors("newPasswordConfirm")
        && !changeDto.getNewPassword().equals(changeDto.getNewPasswordConfirm()) ) {
      bindingResult.rejectValue("newPasswordConfirm", "mismatch", "비밀번호가 일치하지 않습니다.");
    }
    if( bindingResult.hasErrors() ) {
      return "page/mypage/password";
    }
      
    // 비밀번호 변경 요청
    String username = loginUser.getUser().getUsername();
    String newPassword = changeDto.getNewPassword();
    User updatedUser = userService.changePassword(username, newPassword);
    session.removeAttribute(PASSWORD_VERIFIED);

    // 로그인 인증 정보 갱신
    CustomUser updatedLoginUser = new CustomUser(updatedUser);
    Authentication authentication = new UsernamePasswordAuthenticationToken(
                                                    updatedLoginUser,
                                                    updatedLoginUser.getPassword(),
                                                    updatedLoginUser.getAuthorities()
                                                  );
    SecurityContextHolder.getContext().setAuthentication(authentication);       
    ra.addFlashAttribute("message", "비밀번호가 변경되었습니다.");
    return "redirect:/users/mypage/info";
  }
  
  
  
  
}


