package com.aloha.security.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.aloha.security.domain.Board;
import com.aloha.security.dto.CustomUser;
import com.aloha.security.service.BoardService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j 
@Controller 
@RequestMapping("/board")
@RequiredArgsConstructor 
public class BoardController {
  
  private final BoardService boardService;

  // 아래의 요청경로 매핑을 하기 위해서 컨트롤러 메소드를 작성해보세요.
  // 게시글 목록          - [GET]   /board               /board/list.html
  @GetMapping("")
  public String list(Model model) throws Exception {
    List<Board> list = boardService.list();
    model.addAttribute("list", list);
    return "board/list";
  }

  // 게시글 조회          - [GET]   /board/{id}          /board/detail.html
  @PreAuthorize("isAuthenticated()")                    // 인증 체크
  @GetMapping("/{id}")
  public String selet(Model model, @PathVariable("id") String id) throws Exception {
    Board board = boardService.selectById(id);
    model.addAttribute("board", board);
    return "board/detail";
  }


  // 게시글 등록          - [GET]   /board/create        /board/create.html
  // @Secured("ROLE_USER")                   // USER 권한 체크
  // @PreAuthorize("hasRole('USER')")        // USER 권한 체크
  @PreAuthorize("isAuthenticated()")         // 인증 체크
  @GetMapping("/create")
  public String create() {
    return "board/create";
  }


  // 게시글 등록 처리     - [POST]  /board
  // @PreAuthorize("hasRole('USER')")              // USER 권한 체크
  @PreAuthorize("hasAnyRole('USER', 'ADMIN')")     // USER or ADMIN 권한 체크
  @PostMapping("")
  public ResponseEntity<?> create(@AuthenticationPrincipal CustomUser customUser, @RequestBody Board board) {
    try {
      // 현재 인증된 사용자번호(no) 를 등록요청한 게시글 데이터에 세팅
      Long userNo = customUser.getUser().getNo();
      
      boolean result = boardService.insert(board, userNo);
      if( !result ) {
        return new ResponseEntity<>("FAIL", HttpStatus.BAD_REQUEST);// 400
      }
      return new ResponseEntity<>("SUCCESS", HttpStatus.CREATED);// 201
    } catch (Exception e) {
      e.printStackTrace();
      return new ResponseEntity<>("SUCCESS", HttpStatus.INTERNAL_SERVER_ERROR);// 500
    }  
  }



  // 게시글 수정          - [GET]   /board/update/{id}   /board/update.html

  /**
     * 게시글 수정
     * @param model
     * @param id
     * @return
     * @throws Exception
     * ⚡ @PreAuthorize
     * - 파라미터 값을 가져오는 방법 
     *    : #p0, #p1 형태로 파라미터 인덱스를 지정하여 가져올 수 있다.
     * * 여기서는 요청 파라미터로 넘어온 id 2번째에 있기 때문에
     *   인덱스로는 1번이 되서 ➡ #p1
     * - 서비스 메소드를 권한 제어 로직으로 활용하는 방법
     *   : "@빈이름" 형태로 특정 빈의 메소드를 호출할 수 있다.
     * * 여기서는 @BoardService.isOwner( {id}, {userNo} )
     */
  // 👮‍♀️관리자 👩‍🏫작성자 검증
  @PreAuthorize(" (hasRole('ADMIN')) or  #p1 != null and @BoardService.isOwner( #p1, authentication.principal.user.no ) ")
  @GetMapping("/update/{id}")
  public String update(Model model, @PathVariable("id") String id) throws Exception {
    Board board = boardService.selectById(id);
    model.addAttribute("board", board);
    return "board/update";
  }

  // 게시글 수정 처리     - [PUT]    /board
  // 👮‍♀️관리자 👩‍🏫작성자 검증
  @PreAuthorize(" (hasRole('ADMIN')) or #p0 != null and @BoardService.isOwner( #p0.id, authentication.principal.user.no ) ")
  @PutMapping("")
  public ResponseEntity<?> update(@RequestBody Board board) {
    try {
      boolean result = boardService.updateById(board);
      if( !result ) {
        return new ResponseEntity<>("FAIL", HttpStatus.BAD_REQUEST); // 400
      }
      return new ResponseEntity<>("SUCCESS", HttpStatus.OK); // 200
    } catch (Exception e) {
        e.printStackTrace();
      return new ResponseEntity<>("FAIL", HttpStatus.INTERNAL_SERVER_ERROR); // 500
    }  
  }

  // 게시글 삭제 처리     - [DELETE] /board/{id}
  // 👮‍♀️관리자 👩‍🏫작성자 검증
  @PreAuthorize(" (hasRole('ADMIN')) or #p0 != null and @BoardService.isOwner( #p0, authentication.principal.user.no ) ")
  @DeleteMapping("/{id}")
  public ResponseEntity<?> delete(@PathVariable("id") String id) {
    try {
      boolean result = boardService.deleteById(id);
      if(!result) {
        return new ResponseEntity<>("FAIL", HttpStatus.BAD_REQUEST); //400
      }
      return new ResponseEntity<>("SUCCESS", HttpStatus.OK); //200
    } catch (Exception e) {
      e.printStackTrace();
      return new ResponseEntity<>("FAIL", HttpStatus.INTERNAL_SERVER_ERROR); //500
    }
  }
  
}
