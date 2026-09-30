# fetch.js 사용 가이드

브라우저 기본 `fetch` API 기반의 공통 비동기 요청 함수입니다. `ajax.js`의 `$ajax`와 동일한 인터페이스를
가지지만 jQuery 없이 동작합니다. CSRF 토큰을 자동으로 헤더에 첨부합니다.

> ⚠️ 사용 전 페이지에 CSRF 메타 태그(`<meta name="_csrf">`, `<meta name="_csrf_header">`)가 있어야 합니다.

## 함수 시그니처

```js
$fetch(obj)
```

### 파라미터 (obj)

| 속성 | 타입 | 필수 | 설명 |
|---|---|---|---|
| `url` | string | O | 요청 URL |
| `method` | string | O | `'GET'`, `'POST'`, `'PUT'`, `'DELETE'` |
| `data` | Object \| FormData | X | 요청 데이터. `GET`/`DELETE`는 쿼리 문자열(`?key=value`)로, 그 외는 JSON(또는 FormData 그대로)으로 전송 |
| `success` | function(response) | X | 요청 성공(`res.ok === true`) 시 콜백 |
| `error` | function(response) | X | 요청 실패 또는 예외 발생 시 콜백 |

### 반환값

`Promise` — 응답의 `Content-Type`이 `application/json`이면 JSON 객체로, 아니면 텍스트로 파싱하여 반환합니다.

## 사용 예시

### 1. JSON 데이터로 POST 요청

```js
$fetch({
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
$fetch({
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

$fetch({
    url: '/api/upload',
    method: 'POST',
    data: formData,
    success: (res) => console.log('업로드 성공', res)
});
```

### 4. DELETE 요청

```js
$fetch({
    url: '/api/items',
    method: 'DELETE',
    data: { id: 1 },
    success: (res) => console.log('삭제 완료', res)
});
```

### 5. await로 응답값 직접 사용

```js
async function loadItems() {
    const res = await $fetch({ url: '/api/items', method: 'GET' });
    console.log(res);
}
```

## 동작 참고

- CSRF 토큰은 페이지의 `<meta name="_csrf">`, `<meta name="_csrf_header">` 값을 읽어 모든 요청 헤더에 자동으로 추가합니다.
- `data`가 `FormData`가 아닐 경우에만 `Content-Type: application/json` 헤더를 붙입니다. (FormData는 브라우저가 boundary를 포함해 자동 설정)
- `GET`/`DELETE`는 `data` 객체를 `URLSearchParams`로 변환해 쿼리 문자열로 URL에 붙입니다.
- 응답이 실패(`res.ok === false`)이거나 예외가 발생하면 `error` 콜백이 호출되고, 에러(응답) 객체를 그대로 반환합니다.
- `ajax.js`의 `$ajax`와 호출 방식이 동일하므로 jQuery 의존성을 없애고 싶을 때 대체해서 사용할 수 있습니다.
