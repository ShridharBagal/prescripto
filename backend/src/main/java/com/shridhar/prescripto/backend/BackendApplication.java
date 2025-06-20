package com.shridhar.prescripto.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BackendApplication {

	public static void main(String[] args) {
		EnvLoader.loadEnv();
		SpringApplication.run(BackendApplication.class, args);
	}

}