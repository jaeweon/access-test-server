package com.queuesystem.accesstestserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@RestController
@RequestMapping("/api/v1/k6")
public class K6Controller {

    @Autowired
    private ResourceLoader resourceLoader;

    @PostMapping("/start")
    public ResponseEntity<String> startLoadTest() {
        try {
            String scriptPath = "/Users/user/Documents/access-queue-system/access-test-server/src/main/resources/static/test.js"; // 절대 경로 설정
            ProcessBuilder processBuilder = new ProcessBuilder("k6", "run", scriptPath);
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();

            // 로그 출력
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                StringBuilder output = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append("\n");
                    System.out.println(line); // 디버깅용 출력
                }
            }

            int exitCode = process.waitFor();
            if (exitCode == 0) {
                return ResponseEntity.ok("K6 테스트 성공적으로 실행됨");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("K6 실행 실패: Exit Code " + exitCode);
            }
        } catch (IOException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("K6 실행 오류: " + e.getMessage());
        }
    }
}

