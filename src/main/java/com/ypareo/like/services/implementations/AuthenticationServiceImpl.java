package com.ypareo.like.services.implementations;

import com.ypareo.like.dtos.LoginRequestDto;
import com.ypareo.like.dtos.LoginResponseDto;
import com.ypareo.like.repositories.UserRepository;
import com.ypareo.like.services.AuthenticationService;
import com.ypareo.like.services.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Override
    public ResponseEntity<LoginResponseDto> login(LoginRequestDto loginRequestDto) {

            var user = userRepository.findByUsername(loginRequestDto.getUsername()).orElseThrow(
                    () -> new UsernameNotFoundException("Invalid username or password")
            );

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequestDto.getUsername(), loginRequestDto.getPassword())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            var jwtToken = jwtService.generateToken(user);

            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.add("Access-Control-Expose-Headers", "Authorization");
            responseHeaders.add(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken);

        return ResponseEntity.ok()
                .headers(responseHeaders)
                .body(LoginResponseDto.builder()
                        .message("Login successful")
                        .token(jwtToken)
                        .build()
                );
    }
}
