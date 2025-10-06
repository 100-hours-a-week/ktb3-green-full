package com.example.spring_community.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController // 이 클래스가 RESTful 웹 서비스의 컨트롤러임을 나타냅니다.
public class HelloController {

    @GetMapping("/") // HTTP GET 요청이 "/" 경로로 올 때 이 메서드를 실행합니다.
    public String hello() {
        return "Hello, World!"; // 반환된 문자열이 HTTP 응답 본문에 그대로 쓰여집니다.
    }
}