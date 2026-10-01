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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.aloha.shop.domain.users.Address;
import com.aloha.shop.domain.users.User;
import com.aloha.shop.dto.users.AddressDto;
import com.aloha.shop.dto.users.UserJoinDto;
import com.aloha.shop.dto.users.UserUpdateDto;
import com.aloha.shop.security.CustomUser;
import com.aloha.shop.service.users.AddressService;
import com.aloha.shop.service.users.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;






@Slf4j 
@Controller 
@RequestMapping("/users")
@RequiredArgsConstructor 
public class UserController {
  
  private final UserService userService;
  private final AddressService addressService;

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
  
  
  
  

  
}


