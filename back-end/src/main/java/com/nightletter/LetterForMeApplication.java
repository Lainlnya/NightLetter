package com.nightletter;

import java.util.TimeZone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@EnableJpaAuditing
@EnableScheduling
@SpringBootApplication
public class LetterForMeApplication {
	@PostConstruct
	void started(){
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
		log.info("Set Time Zone UTC+9.");
	}

	public static void main(String[] args) {
		SpringApplication.run(LetterForMeApplication.class, args);
	}
}
