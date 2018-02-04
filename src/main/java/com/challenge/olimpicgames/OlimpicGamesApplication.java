package com.challenge.olimpicgames;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.client.RestTemplate;

import com.challenge.olimpicgames.controller.CompetitionController;

@SpringBootApplication
@ComponentScan(basePackages = "com.challenge.olimpicgames")
@EntityScan(basePackages = "com.challenge.olimpicgames")
@EnableJpaRepositories("com.challenge.olimpicgames.repository")
public class OlimpicGamesApplication {

	public static void main(String[] args) {
		SpringApplication.run(OlimpicGamesApplication.class, args);
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	@Bean
	public CompetitionController competitionController() {
		return new CompetitionController();
	}

}
