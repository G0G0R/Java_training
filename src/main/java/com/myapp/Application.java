package com.myapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class 	Application {

	private Application() {
		// utility class
	}

	static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
