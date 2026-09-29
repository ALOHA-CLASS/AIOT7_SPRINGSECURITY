// fetch
async function $fetch( obj ) {
    console.log('data:', obj.data);
    console.log('url:', obj.url);
    console.log('type:', obj.method);

    try {
        // 💎 CSRF TOKEN
        const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
        const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');

        const isFormData = obj.data instanceof FormData;
        const isQueryMethod = obj.method == 'GET' || obj.method == 'DELETE';

        const headers = { [csrfHeader]: csrfToken };
        if (!isFormData) headers['Content-Type'] = 'application/json; charset=utf-8';

        let url = obj.url;
        let body;
        if (isQueryMethod) {
            // GET/DELETE는 데이터를 쿼리 문자열로 변환
            if (obj.data) {
                const params = new URLSearchParams(obj.data).toString();
                if (params) url += (url.includes('?') ? '&' : '?') + params;
            }
        } else {
            body = isFormData ? obj.data : JSON.stringify(obj.data);
        }

        const res = await fetch(url, { method: obj.method, headers: headers, body: body });

        const contentType = res.headers.get('content-type') || '';
        const response = contentType.includes('application/json') ? await res.json() : await res.text();

        if (!res.ok) {
            if (obj.error) obj.error(response);
            return response; // 에러 응답 그대로 반환하여 호출부에서 사용 가능하게 함
        }

        if (obj.success) obj.success(response);
        return response;
    } catch (error) {
        console.log('[fetch.js] error -------------------------');

        // console.error('Error:', error);
        if (obj.error) obj.error(error);
        return error; // 예외 객체 그대로 반환하여 호출부에서 사용 가능하게 함
    }   

}