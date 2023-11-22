package com.postgraduate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class PostgraduateApplication {

	public static void main(String[] args) {
		SpringApplication.run(PostgraduateApplication.class, args);
	}

}
