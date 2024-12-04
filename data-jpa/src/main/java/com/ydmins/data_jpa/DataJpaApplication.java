package com.ydmins.data_jpa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;

import java.util.Optional;
import java.util.UUID;

@SpringBootApplication
public class DataJpaApplication {

	public static void main(String[] args) {
		SpringApplication.run(DataJpaApplication.class, args);
	}

	@Bean
	public AuditorAware<String> auditorProvider(){
		// Spring Security에서 사용자 정보를 불러와 byWho 자리에 넣어주면 된다.
		String byWho = UUID.randomUUID().toString();
		return () -> Optional.of(byWho);
	}
}
