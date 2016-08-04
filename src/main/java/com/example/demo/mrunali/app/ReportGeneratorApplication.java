package com.example.demo.mrunali.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ReportGeneratorApplication {

	private static final Logger logger = LoggerFactory.getLogger(ReportGeneratorApplication.class);

	public static void main(String[] args) {
		logger.info("Starting Report Generation");
		SpringApplication.run(ReportGeneratorApplication.class, args);
		logger.info("Report Generation Complete");
	}

}
