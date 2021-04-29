package com.bandido.app.documents.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import lombok.Data;

@Data
@Configuration
@PropertySource("classpath:application.properties")
@ConfigurationProperties(prefix = "service.config.rabbitmq")
public class RabbitProperties {
	
	private String exchange;
	private String queue;

}
