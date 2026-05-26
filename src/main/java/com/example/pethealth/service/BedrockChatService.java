package com.example.pethealth.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.bedrockruntime.BedrockRuntimeClient;
import software.amazon.awssdk.services.bedrockruntime.model.InvokeModelRequest;
import software.amazon.awssdk.services.bedrockruntime.model.InvokeModelResponse;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BedrockChatService {

    private final BedrockRuntimeClient bedrockRuntimeClient;
    private final ObjectMapper objectMapper;

    @Value("${aws.bedrock.model-id}")
    private String modelId;

    public String chat(String userMessage) {
        try {
            String systemPrompt = """
                    당신은 반려견 건강 관리 서비스의 AI 상담사 '멍코치'입니다.

                    역할:
                    - 사용자의 반려견 건강 관리, 식사, 음수, 배변, 산책, 생활 습관에 대해 친절하게 답변합니다.
                    - 센서 데이터 기반 해석이 필요한 경우, 데이터가 없으면 단정하지 말고 추가 확인이 필요하다고 말합니다.
                    - 질병 진단이나 약 처방처럼 수의학적 판단이 필요한 내용은 단정하지 않고, 동물병원 상담을 권장합니다.
                    - 답변은 한국어로, 너무 길지 않게 3~6문장 정도로 작성합니다.
                    """;

            Map<String, Object> requestBody = Map.of(
                    "messages", List.of(
                            Map.of(
                                    "role", "user",
                                    "content", List.of(
                                            Map.of("text", systemPrompt + "\n\n사용자 질문: " + userMessage)
                                    )
                            )
                    ),
                    "inferenceConfig", Map.of(
                            "maxTokens", 500,
                            "temperature", 0.5,
                            "topP", 0.9
                    )
            );

            String requestJson = objectMapper.writeValueAsString(requestBody);

            InvokeModelRequest request = InvokeModelRequest.builder()
                    .modelId(modelId)
                    .contentType("application/json")
                    .accept("application/json")
                    .body(SdkBytes.fromUtf8String(requestJson))
                    .build();

            InvokeModelResponse response = bedrockRuntimeClient.invokeModel(request);

            String responseJson = response.body().asUtf8String();
            JsonNode root = objectMapper.readTree(responseJson);

            JsonNode textNode = root
                    .path("output")
                    .path("message")
                    .path("content")
                    .get(0)
                    .path("text");

            if (textNode == null || textNode.isMissingNode() || textNode.asText().isBlank()) {
                return "죄송합니다. 지금은 답변을 생성하지 못했습니다. 잠시 후 다시 시도해 주세요.";
            }

            return textNode.asText();

        } catch (Exception e) {
            e.printStackTrace();
            return "죄송합니다. 멍코치 상담 연결 중 오류가 발생했습니다.";
        }
    }
}