package br.com.cinetracker;

import br.com.cinetracker.models.EpisodeData;
import br.com.cinetracker.models.SeasonData;
import br.com.cinetracker.models.SeriesData;
import br.com.cinetracker.principal.Principal;
import br.com.cinetracker.services.ApiService;
import br.com.cinetracker.services.DataConverter;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

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
