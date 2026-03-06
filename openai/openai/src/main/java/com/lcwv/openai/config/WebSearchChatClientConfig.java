package com.lcwv.openai.config;

import com.lcwv.openai.advisors.TokenUsageAuditAdvisor;
import com.lcwv.openai.rag.WebSearchDocumentRetriever;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

import java.util.List;

@Configuration
public class WebSearchChatClientConfig {

    @Bean("WebSearchChatClient")
    public ChatClient chatClient(ChatClient.Builder chatClientBuilder,
                                 ChatMemory chatMemory, RestClient .Builder restClientBuilder
                                 ){
        Advisor loggerAdviser=new SimpleLoggerAdvisor();
        Advisor memoryAdvisor = MessageChatMemoryAdvisor.builder(chatMemory).build();
        Advisor tokenUsageAdvisor = new TokenUsageAuditAdvisor();
        RetrievalAugmentationAdvisor retrievalAugmentationAdvisor = RetrievalAugmentationAdvisor.builder().documentRetriever(
                WebSearchDocumentRetriever.builder().restClientBuilder(restClientBuilder).maxResults(5).build()).build();
        return chatClientBuilder
                .defaultAdvisors(List.of(loggerAdviser,memoryAdvisor,tokenUsageAdvisor,retrievalAugmentationAdvisor))
                .build();
    }
}
