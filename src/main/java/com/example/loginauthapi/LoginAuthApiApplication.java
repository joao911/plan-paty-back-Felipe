package com.example.loginauthapi;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Plan Party", version = "1", description = "API desenvolvida para testes do OpenApi"))
public class LoginAuthApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(LoginAuthApiApplication.class, args);
	}

}
