package com.example;

import java.net.http.HttpClient;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import dev.langchain4j.model.openai.OpenAiStreamingChatModel;

@Configuration
class AiConfig {

    // private static final String OPENVINO_BASE_URL = "http://192.168.0.5:1234/v3";
    private static final String OPENVINO_BASE_URL = "http://127.0.0.1:1234/v1";
    private static final String OPENVINO_API_KEY = "dummy";

    @Bean
    public ChatClient chatClient(OpenAiChatModel chatModel) {
        return ChatClient.builder(chatModel)
                .defaultAdvisors(new SimpleLoggerAdvisor())
                .build();
    }

    @Bean
    public OpenAiChatModel chatModel() {
        OpenAiChatOptions options = OpenAiChatOptions.builder()
                .baseUrl(OPENVINO_BASE_URL)
                .apiKey(OPENVINO_API_KEY)
                // .model("OpenVINO/Qwen3-8B-int4-cw-ov")
                .model("imperfect-follow/qwen3-14b-int4-asym-awq-ov")
                .maxTokens(4096)
                .temperature(0.7)
                .topP(0.8)
                .topK(20)
                .presencePenalty(1.5)
                .build();

        return OpenAiChatModel.builder()
                .options(options)
                .build();
    }

    @Bean
    public OpenAiStreamingChatModel streamingChatModel() {
        return OpenAiStreamingChatModel.builder()
                .baseUrl(OPENVINO_BASE_URL)
                .apiKey(OPENVINO_API_KEY)
                // .modelName("OpenVINO/Qwen2.5-Coder-14B-Instruct-int4-ov")
                // .modelName("OpenVINO/Qwen3-8B-int4-cw-ov")
                .modelName("qwen3.5:9b")
                // .modelName("imperfect-follow/qwen3-14b-int4-asym-awq-ov")
                .maxTokens(4096)
                .temperature(0.7)
                .topP(0.8)
                .presencePenalty(1.5)
                .parallelToolCalls(false)
                .logResponses(true)
                .logRequests(true)
                // .httpClientBuilder(new CustomHttpClientBuilder().httpClientBuilder(HttpClient.newBuilder().version(HttpClient.Version.HTTP_1_1)))
                .maxTokens(4096)
                .build();
    }
}
