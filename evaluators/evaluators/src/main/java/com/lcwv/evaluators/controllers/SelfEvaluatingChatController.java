package com.lcwv.evaluators.controllers;

import com.lcwv.evaluators.exception.InvalidAnswerException;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.evaluation.FactCheckingEvaluator;
import org.springframework.ai.evaluation.EvaluationRequest;
import org.springframework.ai.evaluation.EvaluationResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

@RestController
@RequestMapping("/api/self-evaluating")
public class SelfEvaluatingChatController {

    private final ChatClient chatClient;
    private final FactCheckingEvaluator factCheckingEvaluator;

    @Value("classpath:/promptTemplates/VikHrPolicy.st")
    Resource hrPolicyTemp;

    @Value("classpath:/promptTemplates/factCheck.st")
    Resource factCheckTemp;

    public SelfEvaluatingChatController(
            ChatClient.Builder chatClientBuilder,
            @Value("classpath:/promptTemplates/VikHrPolicy.st")
            Resource hrPolicyTemp,
            @Value("classpath:/promptTemplates/factCheck.st")
            Resource factCheckTemp) throws IOException {

        this.hrPolicyTemp = hrPolicyTemp;
        this.factCheckTemp = factCheckTemp;

        this.chatClient = chatClientBuilder
                .defaultAdvisors(new SimpleLoggerAdvisor())
                .build();

        String factPrompt = new String(
                factCheckTemp.getInputStream().readAllBytes(),
                Charset.defaultCharset());

        this.factCheckingEvaluator =
                FactCheckingEvaluator.builder(chatClientBuilder)
                        .evaluationPrompt(factPrompt)
                        .build();
    }


    @GetMapping("/chat")
    public String chat(@RequestParam("message") String message) {
        String airesponse = chatClient.prompt().user(message)
                .call().content();
        validateAnswer(message,airesponse);
        return airesponse;
    }

    @GetMapping("/prompt-stuffing")
    public String promptStuffing(@RequestParam("message") String message) {
        return chatClient
                .prompt().system(hrPolicyTemp)
                .user(message)
                .call().content();
    }
    private void validateAnswer(String message,String answer){
        EvaluationRequest evaluationRequest=new EvaluationRequest(message, List.of(),answer);
        EvaluationResponse evaluationResponse=factCheckingEvaluator.evaluate(evaluationRequest);
        if (!evaluationResponse.isPass()){
            throw new InvalidAnswerException(message,answer);
        }
    }
}
