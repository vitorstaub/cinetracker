package br.com.cinetracker.principal;

import br.com.cinetracker.models.Episode;
import br.com.cinetracker.models.EpisodeData;
import br.com.cinetracker.models.SeasonData;
import br.com.cinetracker.models.SeriesData;
import br.com.cinetracker.services.ApiService;
import br.com.cinetracker.services.DataConverter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import ch.qos.logback.core.encoder.JsonEscapeUtil;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.beans.factory.annotation.Value;

public class Principal {
    private final String DEFAULT_ADDRESS = "https://www.omdbapi.com/?t=";

    private final Dotenv dotenv = Dotenv.load();
    private final String API_KEY = dotenv.get("API_KEY");

    private final ApiService api = new ApiService();
    private final Scanner scanner = new Scanner(System.in);
    private final DataConverter converter = new DataConverter();

    public void showMenu() {
        System.out.println("Enter series name");
        var seriesName = scanner.nextLine().replace(" ", "+").toLowerCase();

        var json = api.getData(DEFAULT_ADDRESS + seriesName + "&apikey=" + API_KEY);

        SeriesData data = converter.getData(json, SeriesData.class);
        System.out.println(data);

        List<SeasonData> seasons = new ArrayList<>();

        for (int i = 1; i <= data.totalSeasons(); i++) {
            json = api.getData(DEFAULT_ADDRESS + seriesName + "&season=" + i + "&apikey=" + API_KEY);
            SeasonData seasonData = converter.getData(json, SeasonData.class);
            seasons.add(seasonData);
        }

        List<EpisodeData> episodeData = seasons.stream()
                .flatMap(s -> s.episodes().stream())
                .collect(Collectors.toList());

        List<Episode> episodes = seasons.stream()
                .flatMap(s -> s.episodes().stream()
                        .map(d -> new Episode(s.seasonNumber(), d))
                ).toList();

        while (true) {
            System.out.println("\nOptions: \n [0] Exit \n [1] Show all episodes names \n [2] Top 5 episodes \n [3] All episodes info");
            int choice = Integer.parseInt(scanner.nextLine());
            switch (choice) {
                case 0:
                    break;
                case 1:
                    seasons.forEach(s -> s.episodes().forEach(e -> System.out.println(e.title())));
                    break;
                case 2:
                    System.out.println("\nTop 5: ");
                    episodeData.stream()
                            .filter(e -> !e.episodeRating().equalsIgnoreCase("N/A"))
                            .sorted(Comparator.comparing(EpisodeData::episodeRating).reversed())
                            .limit(5)
                            .forEach(System.out::println);
                    break;
                case 3:
                    episodes.forEach(System.out::println);
                    break;
            }
            if (choice == 0) {
                break;
            }

        }






    }
}
