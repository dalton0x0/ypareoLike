package com.ypareo.like.utils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApiError {

    private int status;
    private String error;
    private String message;
    private String path;
    private LocalDateTime timestamp;
    private List<ApiSubError> subErrors;
}
