package com.lcwv.openai.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ChatController {
    private final ChatClient chatClient;

    public ChatController(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    @GetMapping("/chat")
    public String chat(@RequestParam("message") String message) {

        return chatClient
                .prompt()
                .system("""
                            You are an HR Policy Assistant.
                            You must ONLY answer questions related to HR, leave policies, or company policy.
                            If the question is unrelated, respond strictly with:
                            "I'm sorry, I can only assist with HR and company leave policy related questions."
                        """)
                .user(message)
                .call().content();
    }
}
