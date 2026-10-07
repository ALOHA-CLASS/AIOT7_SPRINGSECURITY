

// 주소 검색
function searchAddress() {
  new daum.Postcode({
      // 주소 검색 완료 시
      oncomplete: function(data) {
      // 사용자가 선택한 주소 타입에 맞춰 기본 주소를 구성
      const addr = data.userSelectedType === 'R' ? data.roadAddress : data.jibunAddress;

      $("#zipcode").val(data.zonecode)
      $("#address1").val(addr)
      $("#address2").focus()
      }
  }).open();
}


$(function () {

  // 모바일 오프캔버스(슬라이드) 메뉴 관련 요소
  const $oc = $("#offcanvas");          // 오프캔버스 메뉴 패널
  const $backdrop = $(".offcanvas-backdrop"); // 메뉴 뒤 어두운 배경(클릭 시 닫힘)
  const $toggle = $(".menu-toggle");    // 헤더의 햄버거(메뉴 열기) 버튼

  // 오프캔버스 메뉴 열기
  function openMenu() {
    $oc.addClass("open").attr("aria-hidden", "false");
    $backdrop.prop("hidden", false);
    // display:none → block 적용 직후에 바로 opacity를 올리면 트랜지션이
    // 생략될 수 있어, 다음 프레임에서 show 클래스를 붙여 페이드인 애니메이션을 보장
    requestAnimationFrame(() => $backdrop.addClass("show"));
    $toggle.attr("aria-expanded", "true");
    $("body").addClass("no-scroll"); // 메뉴 열린 동안 배경 스크롤 방지
  }

  // 오프캔버스 메뉴 닫기
  function closeMenu() {
    $oc.removeClass("open").attr("aria-hidden", "true");
    $backdrop.removeClass("show");
    // 닫힘 트랜지션(0.3s)이 끝난 뒤에 배경을 완전히 숨겨
    // 트랜지션 도중 클릭이 막히지 않도록 처리
    setTimeout(() => { if (!$oc.hasClass("open")) $backdrop.prop("hidden", true); }, 300);
    $toggle.attr("aria-expanded", "false");
    $("body").removeClass("no-scroll");
  }

  // 메뉴 열기/닫기 트리거 바인딩
  $toggle.on("click", openMenu);          // 햄버거 버튼 클릭 시 열기
  $(".oc-close").on("click", closeMenu);  // 메뉴 내 닫기(X) 버튼
  $backdrop.on("click", closeMenu);       // 배경 클릭 시 닫기
  $oc.find("a").on("click", closeMenu);   // 메뉴 안 링크 클릭 시 자동 닫기
  $(document).on("keydown", (e) => { if (e.key === "Escape") closeMenu(); }); // ESC 키로 닫기
  // 데스크톱 너비(769px 이상)로 전환되면 오프캔버스가 숨겨진 버튼 상태로
  // 남지 않도록 자동으로 닫아줌
  window.matchMedia("(min-width: 769px)").addEventListener("change", (e) => { if (e.matches) closeMenu(); });



  // 현재 경로에 맞는 하단 내비게이션(Bottom Navigation) 활성화 표시
  const path = location.pathname;
  $(".bottom-nav a").each(function () {
    const match = $(this).data("match"); // data-match 속성에 지정된 매칭 경로
    // "exact"는 메인(/) 전용 완전 일치, 그 외는 해당 경로이거나 하위 경로일 때 활성화
    const active = match === "exact" ? path === "/" : path === match || path.startsWith(match + "/");
    $(this).toggleClass("active", active);
  });

  // Lucide 아이콘 초기화
  if( window.lucide ) lucide.createIcons()
})