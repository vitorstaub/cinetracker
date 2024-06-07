package br.com.cinetracker;

import br.com.cinetracker.services.ApiService;
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
		var api = new ApiService();
		var json = api.getData("https://www.omdbapi.com/?t=gilmore+girls&apikey=a997b1d7");
		System.out.println(json);
	}
}
