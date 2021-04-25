package com.bandido.app.documents;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@RibbonClient(name = "status-service")
@EnableFeignClients
@EnableEurekaClient
@EnableHystrix
@SpringBootApplication
public class DocumentsServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(DocumentsServiceApplication.class, args);
	}

}
