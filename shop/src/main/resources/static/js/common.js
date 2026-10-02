

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