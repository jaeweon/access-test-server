import http from 'k6/http';
import { sleep } from 'k6';

export let options = {
    vus: 5000,
    duration: '15s',
};

export default function () {
    let userId = `dummy_user_${__VU}`;
    let payload = {
        username: userId,
        password: 'defaultPassword123',
    };d

    let res = http.post('http://access-queue-system-login-server-1/api/v1/auth/login', payload);

    if (res.status !== 200 && res.status !== 302) {
        console.error(`User ${userId} 로그인 실패`);
    }

    let randomDelay = Math.random() * 8;
    sleep(randomDelay);
}
