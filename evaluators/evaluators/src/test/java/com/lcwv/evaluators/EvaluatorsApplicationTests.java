package com.lcwv.evaluators;

import com.lcwv.evaluators.controllers.ChatController;
import org.junit.jupiter.api.*;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.evaluation.FactCheckingEvaluator;
import org.springframework.ai.chat.evaluation.RelevancyEvaluator;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.document.Document;
import org.springframework.ai.evaluation.EvaluationRequest;
import org.springframework.ai.evaluation.EvaluationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.test.context.TestPropertySource;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource(properties = {
		"spring.ai.openai.api-key=${OPENAI_API_KEY:test-key}",
"logging.level.org.springframework.ai=DEBUG"})
class EvaluatorsApplicationTests {
	@Autowired
	private ChatController chatController;
	@Autowired
	private ChatModel chatModel;

	private ChatClient chatClient;

	private RelevancyEvaluator relevancyEvaluator;
	private FactCheckingEvaluator factCheckingEvaluator;
	@Value("${test.relevancy.min.score:0.7}")
	private float minRelevancyScore;
	@Value("classpath:/promptTemplates/factCheck.st")
	Resource factCheckTemp;

	@Value("classpath:/promptTemplates/VikHrPolicy.st")
	Resource hrPolicyTemp;


@BeforeEach
void setup() throws Exception {

	ChatClient.Builder builder = ChatClient.builder(chatModel)
			.defaultAdvisors(new SimpleLoggerAdvisor());

	this.chatClient = builder.build();

	String factPrompt = new String(
			factCheckTemp.getInputStream().readAllBytes(),
			Charset.defaultCharset());

	this.factCheckingEvaluator = FactCheckingEvaluator.builder(builder)
			.evaluationPrompt(factPrompt)
			.build();

	this.relevancyEvaluator = RelevancyEvaluator.builder()
			.chatClientBuilder(builder)
			.build();
}

	@Test
	@DisplayName("Should return relevant response for basic geographical question")
	@Timeout(value = 30)
	void evaluateChatControllerResponseRelevancy() {
		//Given
		String question ="What is the capital of india ??? ";
		//When
		String aiResponse = chatController.chat(question);
		EvaluationRequest  evaluationRequest=new EvaluationRequest(question,aiResponse);
		EvaluationResponse evaluationResponse = relevancyEvaluator.evaluate(evaluationRequest);
		Assertions.assertAll(()->assertThat(aiResponse).isNotBlank(),
				() -> assertThat(evaluationResponse.isPass())
						.withFailMessage("""
                                ========================================
                                The answer was not considered relevant.
                                Question: "%s"
                                Response: "%s"
                                ========================================
                                """, question, aiResponse)
						.isTrue(),
				()->assertThat(evaluationResponse.getScore())
						.withFailMessage("""
                                ========================================
                                The score %.2f is lower than the minimum required %.2f.
                                Question: "%s"
                                Response: "%s"
                                ========================================
                                """, evaluationResponse.getScore(), minRelevancyScore, question, aiResponse)
						.isGreaterThan(minRelevancyScore)
		);

	}

	@Test
	@DisplayName("Should return factually correct response fr gravity-related Questions")
	@Timeout(value = 300)
	void evaluateFactAccuracyForGravityQuestions() {
		//Given
		String question ="Who discovered the law of universal gravitation ??? ";
		//When
		String aiResponse = chatController.chat(question);
		EvaluationRequest  evaluationRequest=new EvaluationRequest(question,aiResponse);
		EvaluationResponse evaluationResponse = factCheckingEvaluator.evaluate(evaluationRequest);
		Assertions.assertAll(()->assertThat(aiResponse).isNotBlank(),
				() -> assertThat(evaluationResponse.isPass())
						.withFailMessage("""
                                ========================================
                                The answer was not considered factually correct.
                                Question: "%s"
                                Response: "%s"
                                ========================================
                                """, question, aiResponse)
						.isTrue()
		);

	}

	@Test
	@DisplayName("Should correctly evaluate factual response based on HR policy context")
	@Timeout(value = 300)
	void evaluateHrPolicyQuestionWithRagContext() {

		// Given
		String question = "How many paid leaves do employees get annually?";

		// When
		String aiResponse = chatController.promptStuffing(question);

		String retrievedContext;

		try {
			retrievedContext = new String(
					hrPolicyTemp.getInputStream().readAllBytes(),
					Charset.defaultCharset());

		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		EvaluationRequest evaluationRequest = new EvaluationRequest(
				question,
				List.of(new Document(retrievedContext)),
				aiResponse
		);

		EvaluationResponse evaluationResponse =
				factCheckingEvaluator.evaluate(evaluationRequest);

		Assertions.assertAll(
				() -> assertThat(aiResponse).isNotBlank(),

				() -> assertThat(evaluationResponse.isPass())
						.withFailMessage("""
                            ========================================
                            The answer was not considered factually correct.
                            Question: "%s"
                            Response: "%s"
                            Context: %s
                            ========================================
                            """,
								question,
								aiResponse,
								retrievedContext)
						.isTrue()
		);
	}



}
