package com.example.security7_practice.domain.user.service;

import com.example.security7_practice.domain.user.dto.UserRequestDto;
import com.example.security7_practice.domain.user.entity.UserEntity;
import com.example.security7_practice.domain.user.entity.UserRole;
import com.example.security7_practice.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // 회원가입
    @Transactional
    public void join(UserRequestDto dto) {

        // username 중복 확인
        if(userRepository.existsByUsername(dto.getUsername())) {
            throw new IllegalArgumentException("Username is already in use");
        }

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(dto.getPassword());

        // builder로 entity 매핑
        UserEntity userEntity = UserEntity.builder()
                .username(dto.getUsername())
                .password(encodedPassword)
                .role(UserRole.USER)
                .build();

        // db 저장
        userRepository.save(userEntity);
    }

    // 로그인 정보 불러오기
    @Override
    public UserDetails loadUserByUsername(String username) {

        UserEntity userEntity = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("username not found"));

        return User.builder()
                .username(userEntity.getUsername())
                .password(userEntity.getPassword())
                .roles(userEntity.getRole().toString())
                .build();
    }
}