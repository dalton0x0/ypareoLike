package com.ypareo.like.services;

import com.ypareo.like.dtos.LoginRequestDto;
import com.ypareo.like.dtos.LoginResponseDto;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    ResponseEntity<LoginResponseDto> login(LoginRequestDto loginRequestDto);
}
