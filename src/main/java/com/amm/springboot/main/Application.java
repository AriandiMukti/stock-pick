package com.amm.springboot.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableCaching
@ComponentScan(basePackages = { "com.amm.springboot.controller", "com.amm.springboot.service", "com.amm.springboot.repository"} )
public class Application  {
	
	public static void main(String[] args) {

		SpringApplication.run(Application.class, args);
		
	}
}
