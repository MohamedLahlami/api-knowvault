package com.norsys.knowvault;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class KnowVaultApplication {

	public static void main(String[] args) {
		log.info("Starting KnowVault Application...");
		SpringApplication.run(KnowVaultApplication.class, args);
		log.info("KnowVault Application started successfully!");
	}

}
