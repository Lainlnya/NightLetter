package com.nightletter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class LetterForMeApplication {

	public static void main(String[] args) {
		SpringApplication.run(LetterForMeApplication.class, args);
	}
}
