package br.com.cinetracker;

import br.com.cinetracker.principal.Principal;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CineTrackerApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(CineTrackerApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Principal principal = new Principal();
		principal.showMenu();
	}
}
