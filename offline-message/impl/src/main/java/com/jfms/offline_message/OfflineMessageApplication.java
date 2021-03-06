package com.jfms.offline_message;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class OfflineMessageApplication {

	public static void main(String[] args) {
		SpringApplication.run(OfflineMessageApplication.class, args);
	}
}
