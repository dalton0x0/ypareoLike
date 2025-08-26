package com.ypareo.like.dtos;

import com.ypareo.like.enums.RoleType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoleRequestDto {
    private Long id;
    private RoleType name;
}
