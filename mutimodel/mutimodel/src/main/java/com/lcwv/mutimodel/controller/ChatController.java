package com.lcwv.mutimodel.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ChatController {
    private final ChatClient openAIchatClient;
    private final ChatClient ollamaAIchatClient;

    public ChatController(@Qualifier("openAiChatClient") ChatClient openAIchatClient,@Qualifier("ollamaAiChatClient") ChatClient ollamaAIchatClient) {
        this.openAIchatClient = openAIchatClient;
        this.ollamaAIchatClient = ollamaAIchatClient;
    }

    @GetMapping("/openai/chat")
    public String openAIchat(@RequestParam("message") String message) {

        return openAIchatClient.prompt(message).call().content();
    }

    @GetMapping("/ollama/chat")
    public String ollamaAIchat(@RequestParam("message") String message) {

        return ollamaAIchatClient.prompt(message).call().content();
    }
}
