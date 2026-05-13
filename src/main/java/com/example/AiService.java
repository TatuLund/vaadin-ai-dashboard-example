package com.example;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class AiService {

    private final ChatClient chatClient;

    public AiService(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    public String ask(String prompt) {
        return chatClient
                .prompt(prompt)
                .call()
                .content();
    }
}
