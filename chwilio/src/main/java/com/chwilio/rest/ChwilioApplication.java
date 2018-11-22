package com.chwilio.rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = { "com.chwilio" })
public class ChwilioApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChwilioApplication.class, args);
	}
}
