package br.com.cinetracker;

import br.com.cinetracker.models.EpisodeData;
import br.com.cinetracker.models.SeasonData;
import br.com.cinetracker.models.SeriesData;
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
		var api = new ApiService();
		var json = api.getData("https://www.omdbapi.com/?t=gilmore+girls&apikey=a997b1d7");

		DataConverter converter = new DataConverter();
		SeriesData data = converter.getData(json, SeriesData.class);

		System.out.println(data);

		json = api.getData("https://www.omdbapi.com/?t=gilmore+girls&season=1&episode=1&apikey=a997b1d7");

		EpisodeData episodeData = converter.getData(json, EpisodeData.class);

		System.out.println(episodeData);

		List<SeasonData> seasons = new ArrayList<>();

		for (int i = 1; i <= data.totalSeasons(); i++) {
			json = api.getData("https://www.omdbapi.com/?t=gilmore+girls&season=" + i + "&apikey=a997b1d7");
			SeasonData seasonData = converter.getData(json, SeasonData.class);
			seasons.add(seasonData);
		}

		seasons.forEach(System.out::println);
	}
}
