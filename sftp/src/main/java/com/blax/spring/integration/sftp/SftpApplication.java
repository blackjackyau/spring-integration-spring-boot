package com.blax.spring.integration.sftp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.config.EnableIntegration;

@SpringBootApplication
@EnableIntegration
public class SftpApplication {

	public static void main(String[] args) {
		SpringApplication.run(SftpApplication.class, args);
	}

}

