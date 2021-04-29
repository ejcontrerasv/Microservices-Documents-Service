package com.bandido.app.documents.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.IPing;
import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.PingUrl;
import com.netflix.loadbalancer.WeightedResponseTimeRule;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class RibbonConfigurator {
	
	@Autowired
	IClientConfig ribbonClientConfig;
	
	@Bean
	public IPing ribbonPing(IClientConfig config) {
		return new PingUrl();
	}
	
	@Bean
	public IRule ribbonRule(IClientConfig config) {
		log.info("### Entrando en Regla personalizada ###");
		return new WeightedResponseTimeRule();
	}
	
}
