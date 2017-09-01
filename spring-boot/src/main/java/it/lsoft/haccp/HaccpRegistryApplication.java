package it.lsoft.haccp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HaccpRegistryApplication {

	public static void main(String[] args) {
		SpringApplication.run(new Object[] { HaccpRegistryApplication.class, MailService.class }, args);
	}
}
