package com.lcwv.openai.config;

import com.lcwv.openai.advisors.TokenUsageAuditAdvisor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class ChatClientConfig {
    @Bean
    public ChatClient chatClient(ChatClient.Builder chatClientBuilder){

        //Can also be configured in Application.properties for profile specific approach
        ChatOptions chatOptions = ChatOptions.builder().model("gpt-4.1-mini")
               // .maxTokens(10)
                .temperature(0.8)
                .build();

        return chatClientBuilder
                .defaultOptions(chatOptions)
                .defaultAdvisors(List.of(new SimpleLoggerAdvisor(),
                        new TokenUsageAuditAdvisor())   )
                .defaultSystem("""
                        You are an HR Policy Assistant.
                                    You must ONLY answer questions related to HR, leave policies, or company policy.
                                    If the question is unrelated, respond strictly with:
                                    "I'm sorry, I can only assist with HR and company leave policy related questions.
                        """)
                .defaultUser("""
                        How can you help me ???
                        """)
                .build();
    }
}
