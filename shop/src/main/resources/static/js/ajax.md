# ajax.js 사용 가이드

jQuery `$.ajax` 기반의 공통 비동기 요청 함수입니다. CSRF 토큰을 자동으로 헤더에 첨부해주며,
`GET`/`DELETE`와 `POST`/`PUT`의 데이터 처리 방식을 자동으로 구분합니다.

> ⚠️ 사용 전 jQuery, 그리고 페이지에 CSRF 메타 태그(`<meta name="_csrf">`, `<meta name="_csrf_header">`)가 있어야 합니다.

## 함수 시그니처

```js
$ajax(obj)
```

### 파라미터 (obj)

| 속성 | 타입 | 필수 | 설명 |
|---|---|---|---|
| `url` | string | O | 요청 URL |
| `method` | string | O | `'GET'`, `'POST'`, `'PUT'`, `'DELETE'` |
| `data` | Object \| FormData | X | 요청 데이터. `GET`/`DELETE`는 쿼리 문자열로, 그 외는 JSON(또는 FormData 그대로)으로 전송 |
| `success` | function(response) | X | 요청 성공 시 콜백 |
| `error` | function(response) | X | 요청 실패 시 콜백 |

### 반환값

`Promise` — 성공/실패에 관계없이 서버 응답(또는 에러 응답 본문)을 반환합니다.

## 사용 예시

### 1. JSON 데이터로 POST 요청

```js
$ajax({
    url: '/api/items',
    method: 'POST',
    data: { name: '상품명', price: 1000 },
    success: (res) => {
        console.log('등록 성공', res);
    },
    error: (res) => {
        console.log('등록 실패', res);
    }
});
```

### 2. GET 요청 (쿼리 파라미터로 전달)

```js
$ajax({
    url: '/api/items',
    method: 'GET',
    data: { page: 1, size: 10 },
    success: (res) => {
        console.log('목록 조회', res);
    }
});
```

### 3. FormData로 파일 업로드 (POST)

```js
const formData = new FormData();
formData.append('file', fileInput.files[0]);
formData.append('title', '제목');

$ajax({
    url: '/api/upload',
    method: 'POST',
    data: formData,
    success: (res) => console.log('업로드 성공', res)
});
```

### 4. DELETE 요청

```js
$ajax({
    url: '/api/items',
    method: 'DELETE',
    data: { id: 1 },
    success: (res) => console.log('삭제 완료', res)
});
```

### 5. await로 응답값 직접 사용

```js
async function loadItems() {
    const res = await $ajax({ url: '/api/items', method: 'GET' });
    console.log(res);
}
```

## 동작 참고

- CSRF 토큰은 페이지의 `<meta name="_csrf">`, `<meta name="_csrf_header">` 값을 읽어 모든 요청 헤더에 자동으로 추가합니다.
- `data`가 `FormData`인 경우 `contentType`/`processData`를 `false`로 설정하여 브라우저가 boundary를 자동 처리하도록 합니다.
- 에러 발생 시 서버가 내려준 응답 본문(JSON 또는 텍스트)을 그대로 반환하므로, 호출부에서 에러 메시지를 그대로 활용할 수 있습니다.
