package com.ypareo.like.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRequestDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String username;
    private String password;
}
