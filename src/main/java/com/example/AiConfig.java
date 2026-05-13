package com.example;

import java.time.Duration;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.openai.client.OpenAIClient;
import com.openai.client.OpenAIClientAsync;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.client.okhttp.OpenAIOkHttpClientAsync;

@Configuration
class AiConfig {

    @Bean
    public ChatClient chatClient(OpenAiChatModel chatModel) {
        return ChatClient.builder(chatModel)
                .defaultAdvisors(new SimpleLoggerAdvisor())
                .build();
    }

    @Bean
    public OpenAiChatModel chatModel() {
        OpenAIClient openAiClient = OpenAIOkHttpClient.builder()
                .baseUrl("http://192.168.0.18:1234/v1")
                .apiKey("dummy")
                .timeout(Duration.ofMinutes(10))
                .maxRetries(0)
                .build();

        OpenAIClientAsync openAiClientAsync = OpenAIOkHttpClientAsync.builder()
                .baseUrl("http://192.168.0.18:1234/v1")
                .apiKey("dummy")
                .timeout(Duration.ofMinutes(10))
                .maxRetries(0)
                .build();

        OpenAiChatOptions options = OpenAiChatOptions.builder()
                // .model("gemma4-it:e4b")
                .model("google/gemma-4-26b-a4b")
                .maxTokens(4096)
                .temperature(0.7)
                .build();

        return OpenAiChatModel.builder()
                .openAiClient(openAiClient)
                .openAiClientAsync(openAiClientAsync)
                .options(options)
                .build();
    }
}
