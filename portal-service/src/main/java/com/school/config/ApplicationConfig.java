package com.school.config;

import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.aspectj.EnableSpringConfigured;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@ComponentScan(basePackages = "com.school")
@EntityScan(basePackages = "com.school.domain")
@EnableJpaRepositories(basePackages = "com.school.repository")
@EnableTransactionManagement
@EnableSpringConfigured
@EnableAutoConfiguration
@EnableScheduling
@SpringBootApplication
public class ApplicationConfig {

	private  static final Logger LOGGER = LoggerFactory.getLogger(ApplicationConfig.class);

	@Bean
	public Mapper mapper() {
		Mapper mapper = new DozerBeanMapper();
		return mapper;
	}

	public static void main(String[] args) {
		SpringApplication.run(ApplicationConfig.class, args);
	}

}
