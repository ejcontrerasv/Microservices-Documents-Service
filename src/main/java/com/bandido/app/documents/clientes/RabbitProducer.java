package com.bandido.app.documents.clientes;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Autowired;

import com.bandido.app.documents.config.RabbitProperties;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class RabbitProducer {
	
	@Autowired
	private AmqpTemplate rabbitTemp;
	
	@Autowired
	private TopicExchange topicExchange;
	
	@Autowired
	private RabbitProperties properties;
	
	public void sendMessage(String message) {
		rabbitTemp.convertAndSend(topicExchange.getName(), properties.getQueue(), message);
	}

}
