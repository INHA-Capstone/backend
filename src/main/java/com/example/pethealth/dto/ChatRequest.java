package com.example.pethealth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ChatRequest {

    @NotBlank(message = "message는 비어 있을 수 없습니다.")
    private String message;
}