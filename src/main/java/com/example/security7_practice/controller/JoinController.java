package com.example.security7_practice.controller;

import com.example.security7_practice.domain.user.dto.UserRequestDto;
import com.example.security7_practice.domain.user.entity.UserEntity;
import com.example.security7_practice.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class JoinController {

    private final UserService userService;

    // 회원가입 페이지
    @GetMapping("/user/join")
    public String join() {
        return "join";
    }

    // 회원가입 진행
    @PostMapping("/user/join")
    public String join(UserRequestDto dto) {
        userService.join(dto);

        return "redirect:/";
    }
}
