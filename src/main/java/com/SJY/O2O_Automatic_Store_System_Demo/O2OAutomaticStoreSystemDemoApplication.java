package com.SJY.O2O_Automatic_Store_System_Demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class O2OAutomaticStoreSystemDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(O2OAutomaticStoreSystemDemoApplication.class, args);
	}

}
