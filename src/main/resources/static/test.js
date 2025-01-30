import http from 'k6/http';
import { sleep } from 'k6';

export let options = {
    vus: 2000,  // 동시 사용자 1000명
    duration: '15s',  // 테스트 지속 시간
};

export default function () {
    let userId = `dummy_user_${__VU}`; // 가상 유저 ID 생성
    let payload = {
        username: userId,
        password: 'defaultPassword123',
    };

    let res = http.post('http://127.0.0.1:9000/api/v1/auth/login', payload);

    if (res.status !== 200 && res.status !== 302) {
        console.error(`User ${userId} 로그인 실패`);
    }

    // 🔹 각 요청에 무작위 지연 시간 (0~2초 사이) 적용
    let randomDelay = Math.random() * 8;  // 0~2초 랜덤 값
    sleep(randomDelay);
}
