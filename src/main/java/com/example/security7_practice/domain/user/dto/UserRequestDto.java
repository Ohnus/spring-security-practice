package com.example.security7_practice.domain.user.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
// 클라이언트 측에서 dto를 통해 데이터를 받으려면 setter나 생성자로 매핑해야 함
// setter 대신 생성자 만들고 호출 PROTECTED
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class UserRequestDto {

    private String username;
    private String password;
}
