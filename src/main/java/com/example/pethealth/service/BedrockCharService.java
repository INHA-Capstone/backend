package com.example.pethealth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.bedrockruntime.BedrockRuntimeClient;
import software.amazon.awssdk.services.bedrockruntime.model.ContentBlock;
import software.amazon.awssdk.services.bedrockruntime.model.ConversationRole;
import software.amazon.awssdk.services.bedrockruntime.model.ConverseRequest;
import software.amazon.awssdk.services.bedrockruntime.model.ConverseResponse;
import software.amazon.awssdk.services.bedrockruntime.model.InferenceConfiguration;
import software.amazon.awssdk.services.bedrockruntime.model.Message;

@Service
@RequiredArgsConstructor
public class BedrockChatService {

    private final BedrockRuntimeClient bedrockRuntimeClient;

    @Value("${aws.bedrock.model-id}")
    private String modelId;

    public String chat(String userMessage) {
        String systemPrompt = """
                당신은 반려견 건강 관리 서비스의 AI 상담사 '멍코치'입니다.

                역할:
                - 사용자의 반려견 건강 관리, 식사, 음수, 배변, 산책, 생활 습관에 대해 친절하게 답변합니다.
                - 센서 데이터 기반 해석이 필요한 경우, 데이터가 없으면 단정하지 말고 추가 확인이 필요하다고 말합니다.
                - 질병 진단이나 약 처방처럼 수의학적 판단이 필요한 내용은 단정하지 않고, 동물병원 상담을 권장합니다.
                - 답변은 한국어로, 너무 길지 않게 3~6문장 정도로 작성합니다.
                """;

        Message user = Message.builder()
                .role(ConversationRole.USER)
                .content(ContentBlock.fromText(userMessage))
                .build();

        ConverseRequest request = ConverseRequest.builder()
                .modelId(modelId)
                .system(ContentBlock.fromText(systemPrompt))
                .messages(user)
                .inferenceConfig(InferenceConfiguration.builder()
                        .maxTokens(500)
                        .temperature(0.5F)
                        .topP(0.9F)
                        .build())
                .build();

        ConverseResponse response = bedrockRuntimeClient.converse(request);

        if (response.output() == null
                || response.output().message() == null
                || response.output().message().content().isEmpty()) {
            return "죄송합니다. 지금은 답변을 생성하지 못했습니다. 잠시 후 다시 시도해 주세요.";
        }

        return response.output().message().content().get(0).text();
    }
}