package com.lcwv.openai.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import static org.springframework.ai.chat.memory.ChatMemory.CONVERSATION_ID;

@RestController
@RequestMapping("/api/rag")
public class RAGController {
    private final ChatClient chatClient;
    private final ChatClient webSearchChatClient;
    private final VectorStore vectorStore;

    public RAGController(@Qualifier("chatMemoryChatClient") ChatClient chatClient,
                         @Qualifier("WebSearchChatClient") ChatClient webSearchChatClient
            , VectorStore vectorStore) {
        this.chatClient = chatClient;
        this.webSearchChatClient = webSearchChatClient;
        this.vectorStore = vectorStore;
    }
    @Value("classpath:/promptTemplates/systemPromptRandomDataTemplate.st")
    Resource promptTemplate;
    @Value("classpath:/promptTemplates/systemPromptHRpolicyTemplate.st")
    Resource hrPolicyTemplate;
    @GetMapping("/random/chat")
    public ResponseEntity<String> randomChat(@RequestHeader("username") String username,
                                             @RequestParam("message") String message) {
        SearchRequest searchRequest = SearchRequest.builder().query(message).topK(3).similarityThreshold(0.5).build();
        List<Document> documents = vectorStore.similaritySearch(searchRequest);
        String similarContext = documents.stream().map(Document::getText).collect(Collectors.joining(System.lineSeparator()));
        String answer = chatClient.prompt().system(promptSystemSpec -> promptSystemSpec.text(promptTemplate)
                        .param("documents", similarContext))
                .advisors(advisorSpec -> advisorSpec.param(CONVERSATION_ID, username))
                .user(message)
                .call().content();
        return ResponseEntity.ok(answer);

    }
    @GetMapping("/document/chat")
    public ResponseEntity<String> documnetChat(@RequestHeader("username") String username,
                                             @RequestParam("message") String message) {
//        SearchRequest searchRequest = SearchRequest.builder().query(message).topK(3).similarityThreshold(0.5).build();
//        List<Document> documents = vectorStore.similaritySearch(searchRequest);
//        String similarContext = documents.stream().map(Document::getText).collect(Collectors.joining(System.lineSeparator()));
        String answer = chatClient.prompt()
//                .system( promptSystemSpec -> promptSystemSpec.text(hrPolicyTemplate)
//                        .param("documents", similarContext))
                .advisors(advisorSpec -> advisorSpec.param(CONVERSATION_ID, username))
                .user(message)
                .call().content();
        return ResponseEntity.ok(answer);

    }

    @GetMapping("/web-search/chat")
    public ResponseEntity<String> webChat(@RequestHeader("username") String username,
                                               @RequestParam("message") String message) {
        String answer = webSearchChatClient.prompt()
                .advisors(advisorSpec -> advisorSpec.param(CONVERSATION_ID, username))
                .user(message)
                .call().content();
        return ResponseEntity.ok(answer);

    }

    }
