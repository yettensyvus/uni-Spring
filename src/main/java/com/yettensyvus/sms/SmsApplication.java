package com.yettensyvus.sms;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

@ImportResource("classpath:applicationContext.xml") // Import the XML configuration
@SpringBootApplication
public class SmsApplication implements CommandLineRunner {

	@Autowired
	private ApplicationContext context;

	public static void main(String[] args) {
		SpringApplication.run(SmsApplication.class, args);
	}

	@Override
	public void run(String... args) {
		System.out.println("Application has started successfully!");

		// Retrieve the beans and print their data
		Profesor profesor_constr = profesor();
		Profesor profesor_xml = (Profesor) context.getBean("profesor1");

		Curs curs = curs();
		Curs curs_xml = (Curs) context.getBean("curs1");

		// Use setter injection
		curs.setProfesor(profesor_constr);

		System.out.println("Profesor details (Constructor Dependency: " + profesor_constr);
		System.out.println("Profesor details (XML Dependency: " + profesor_xml);
		System.out.println("Curs details (Setter Injection): " + curs);
		System.out.println("Curs details (XML Injection): " + curs_xml);
	}


	// Constructor Injection

	@Bean
	public Profesor profesor() {
		return new Profesor("Ion Popescu", "Matematica", 5);
	}

	@Bean
	public Curs curs() {
		return new Curs("Algebra", 6, null); // Initially set profesor to null
	}
}
