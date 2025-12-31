package com.example.ProfieService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * User Service Application
 * Handles user profile management and user listing
 */
@SpringBootApplication
@EnableEurekaClient
public class ProfieServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProfieServiceApplication.class, args);
	}

}
