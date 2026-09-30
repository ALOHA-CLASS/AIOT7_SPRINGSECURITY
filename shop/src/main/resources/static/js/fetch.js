// fetch
async function $fetch( obj ) {
    console.log('data:', obj.data);
    console.log('url:', obj.url);
    console.log('method:', obj.method);

    try {
        // 💎 CSRF TOKEN
        const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
        const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');

        // 데이터 타입 확인 (폼데이터, JSON)
        const isFormData = obj.data instanceof FormData;
        // 요청 메소드 확인
        const isQueryMethod = obj.method == 'GET' || obj.method == 'DELETE';

        const headers = { [csrfHeader]: csrfToken };
        if (!isFormData) headers['Content-Type'] = 'application/json; charset=utf-8';

        let url = obj.url;
        let body;
        if (isQueryMethod) {
            // GET/DELETE는 데이터를 쿼리 문자열로 변환
            // GET 또는 DELETE 방식이면, 쿼리스트링에 데이터 지정 ➡ /url?data1=10&data=20
            if (obj.data) {
                const params = new URLSearchParams(obj.data).toString();
                if (params) url += (url.includes('?') ? '&' : '?') + params;
            }
        } else {
            // POST 또는 DELETE 방식이면, 
            // 폼 데이터 ➡ body 에  그대로
            // 아니면 ➡ JSON 문자열로 변환
            body = isFormData ? obj.data : JSON.stringify(obj.data);
        }

        // 비동기 요청
        const res = await fetch(url, { method: obj.method, headers: headers, body: body });

        // 응답 헤더에서 컨텐츠타입 확인
        const contentType = res.headers.get('content-type') || '';

        // 컨텐츠 타입 헤더가 json 형식이면, JSON 문자열에서 JS객체로 변환
        const response = contentType.includes('application/json') ? await res.json() : await res.text();

        // 요청 실패 시
        if (!res.ok) {
            if (obj.error) obj.error(response);
            return response; // 에러 응답 그대로 반환하여 호출부에서 사용 가능하게 함
        }

        // 요청 성공 시
        if (obj.success) obj.success(response);
        return response;
    } catch (error) {
        console.log('[fetch.js] error -------------------------');

        // console.error('Error:', error);
        if (obj.error) obj.error(error);
        return error; // 예외 객체 그대로 반환하여 호출부에서 사용 가능하게 함
    }   

}