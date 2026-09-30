# alert.js 사용 가이드

[SweetAlert2](https://sweetalert2.github.io/)(`Swal`) 기반의 공통 알림/확인/토스트 함수 모음입니다.
모든 함수는 **일반 매개변수 방식**과 **객체(`{}`) 매개변수 방식**을 동시에 지원합니다.
(첫 번째 인자가 객체이면 자동으로 객체 속성을 읽어 처리합니다.)

> ⚠️ 사용 전 SweetAlert2(`Swal`) 라이브러리가 로드되어 있어야 합니다.

## 함수 목록

| 함수 | 용도 |
|---|---|
| `$alert` | 기본 알림창 |
| `$alert_` | 기본 알림창 (Promise 반환, 콜백 체이닝용) |
| `$alertHTML` | HTML 콘텐츠를 지원하는 알림창 |
| `$confirm` | 확인/취소 2버튼 알림창 |
| `$confirmDeny` | 확인/거부/취소 3버튼 알림창 |
| `$confirmHTML` | HTML 콘텐츠를 지원하는 확인창 |
| `$toast` | 토스트 알림 |
| `$toast_` | 토스트 알림 (Promise 반환, 콜백 체이닝용) |

---

## $alert / $alert_

기본 알림창을 띄웁니다. `$alert_`는 `$alert`과 동일하지만 이름 그대로 Promise 체이닝(`.then()`)을 강조하는 버전입니다.

```js
$alert(title, text, icon, confirmButtonText = '확인')
```

### 일반 매개변수 방식

```js
$alert('완료', '저장되었습니다.', 'success');
```

### 객체 방식

```js
$alert({ title: '완료', text: '저장되었습니다.', icon: 'success', confirmButtonText: '확인' });
```

### 콜백 체이닝 (`$alert_`)

```js
$alert_({ title: '완료', text: '저장되었습니다.', icon: 'success' })
    .then(() => {
        location.href = '/items';
    });
```

---

## $alertHTML

`text` 대신 `html`을 사용하여 HTML 태그가 포함된 알림창을 띄웁니다.

```js
$alertHTML(title, html, icon, confirmButtonText = '확인')
```

### 일반 매개변수 방식

```js
$alertHTML('안내', '<b>굵은 글씨</b>로 표시됩니다.', 'info');
```

### 객체 방식

```js
$alertHTML({ title: '안내', html: '<b>굵은 글씨</b>로 표시됩니다.', icon: 'info' });
```

---

## $confirm

확인/취소 2개의 버튼을 가진 알림창입니다. `Swal.fire()`의 결과(Promise)를 반환하므로
`result.isConfirmed`로 사용자의 선택을 확인합니다.

```js
$confirm(title, text, icon, confirmButtonText, cancelButtonText, confirmButtonColor = '#3085d6', cancelButtonColor = '#d33')
```

### 일반 매개변수 방식

```js
$confirm('삭제하시겠습니까?', '삭제 후 복구할 수 없습니다.', 'warning', '삭제', '취소')
    .then((result) => {
        if (result.isConfirmed) {
            // 삭제 로직 실행
        }
    });
```

### 객체 방식

```js
$confirm({
    title: '삭제하시겠습니까?',
    text: '삭제 후 복구할 수 없습니다.',
    icon: 'warning',
    confirmButtonText: '삭제',
    cancelButtonText: '취소'
}).then((result) => {
    if (result.isConfirmed) {
        // 삭제 로직 실행
    }
});
```

---

## $confirmDeny

확인/거부/취소 3개의 버튼을 가진 알림창입니다. `result.isConfirmed`, `result.isDenied`로 선택을 구분합니다.

```js
$confirmDeny(title, text, icon, confirmButtonText, denyButtonText, cancelButtonText, confirmButtonColor = '#3085d6', denyButtonColor = '#d33', cancelButtonColor = '#666')
```

### 일반 매개변수 방식

```js
$confirmDeny('저장하시겠습니까?', '변경 사항이 있습니다.', 'question', '저장', '저장 안 함', '취소')
    .then((result) => {
        if (result.isConfirmed) { /* 저장 */ }
        else if (result.isDenied) { /* 저장 안 함 */ }
    });
```

### 객체 방식

```js
$confirmDeny({
    title: '저장하시겠습니까?',
    text: '변경 사항이 있습니다.',
    icon: 'question',
    confirmButtonText: '저장',
    denyButtonText: '저장 안 함',
    cancelButtonText: '취소'
}).then((result) => {
    if (result.isConfirmed) { /* 저장 */ }
    else if (result.isDenied) { /* 저장 안 함 */ }
});
```

---

## $confirmHTML

`text` 대신 `html`을 사용하는 확인창입니다.

```js
$confirmHTML(title, html, icon, confirmButtonText, cancelButtonText, confirmButtonColor = '#3085d6', cancelButtonColor = '#d33')
```

### 일반 매개변수 방식

```js
$confirmHTML('안내', '<b>정말</b> 진행할까요?', 'warning', '진행', '취소')
    .then((result) => {
        if (result.isConfirmed) { /* 진행 */ }
    });
```

### 객체 방식

```js
$confirmHTML({
    title: '안내',
    html: '<b>정말</b> 진행할까요?',
    icon: 'warning',
    confirmButtonText: '진행',
    cancelButtonText: '취소'
});
```

---

## $toast / $toast_

화면 우측 상단(기본값) 등에 잠깐 표시되는 토스트 알림입니다. 처음부터 객체(`obj`) 매개변수만 지원합니다.
`$toast_`는 `Swal.fire()`의 Promise를 반환하여 체이닝이 필요할 때 사용합니다.

```js
$toast(obj)
```

### obj 옵션

| 속성 | 기본값 | 설명 |
|---|---|---|
| `title` | `'success'` | 표시할 메시지 |
| `icon` | `'success'` | `'success'`, `'error'`, `'warning'`, `'info'`, `'question'` |
| `timer` | `3000` | 자동으로 닫히는 시간(ms) |
| `position` | `'top-end'` | 표시 위치 |
| `showConfirmButton` | `false` | 확인 버튼 표시 여부 |
| `timerProgressBar` | `true` | 타이머 진행바 표시 여부 |

### 사용 예시

```js
$toast({ title: '저장되었습니다.', icon: 'success' });

$toast({ title: '오류가 발생했습니다.', icon: 'error', position: 'top', timer: 2000 });
```

### 콜백 체이닝 (`$toast_`)

```js
$toast_({ title: '저장되었습니다.', icon: 'success' })
    .then((result) => {
        if (result.dismiss === Swal.DismissReason.timer) {
            console.log('타이머로 닫힘');
        }
    });
```

## 공통 참고

- `$alert`, `$alert_`, `$alertHTML`, `$confirm`, `$confirmDeny`, `$confirmHTML`은 모두 첫 번째 인자로
  일반 값(`title`)을 넘기거나, 필요한 옵션을 담은 객체 하나만 넘기는 두 가지 방식을 모두 지원합니다.
- 객체 방식 사용 시 각 함수가 필요로 하는 속성명(`title`, `text`/`html`, `icon`, `confirmButtonText` 등)을
  그대로 키로 사용하면 됩니다.
