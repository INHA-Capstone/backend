package com.example.pethealth.controller;

import com.example.pethealth.dto.ChatRequest;
import com.example.pethealth.dto.ChatResponse;
import com.example.pethealth.service.BedrockChatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

    private final BedrockChatService bedrockChatService;

    @PostMapping
    public ChatResponse chat(@Valid @RequestBody ChatRequest request) {
        String answer = bedrockChatService.chat(request.getMessage());
        return new ChatResponse(answer);
    }
}