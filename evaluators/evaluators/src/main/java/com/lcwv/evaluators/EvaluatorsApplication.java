package com.lcwv.evaluators;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;

@SpringBootApplication
@EnableRetry
public class EvaluatorsApplication {

	public static void main(String[] args) {
		SpringApplication.run(EvaluatorsApplication.class, args);
	}

}
