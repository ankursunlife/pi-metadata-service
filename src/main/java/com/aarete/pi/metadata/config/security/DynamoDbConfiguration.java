package com.aarete.pi.metadata.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;

@Configuration
public class DynamoDbConfiguration {

	@Bean
	public AmazonDynamoDB buildAmazonDynamoDB() {
		return AmazonDynamoDBClientBuilder.standard()
				.build();
	}
	
}
