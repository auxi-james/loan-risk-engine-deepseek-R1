package com.loanrisk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class LoanRiskApplication {

	public static void main(String[] args) {
		SpringApplication.run(LoanRiskApplication.class, args);
	}

}
