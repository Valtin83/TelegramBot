package com.example.SpringDemoBot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan
public class SpringDemoBotApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringDemoBotApplication.class, args);
	}

}
