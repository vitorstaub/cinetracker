package br.com.cinetracker.principal;

import br.com.cinetracker.models.Episode;
import br.com.cinetracker.models.EpisodeData;
import br.com.cinetracker.models.SeasonData;
import br.com.cinetracker.models.SeriesData;
import br.com.cinetracker.services.ApiService;
import br.com.cinetracker.services.DataConverter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import io.github.cdimascio.dotenv.Dotenv;


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

        try {
            SeriesData data = converter.convertData(json, SeriesData.class);
            System.out.println(data);

            List<SeasonData> seasons = new ArrayList<>();

            for (int i = 1; i <= data.totalSeasons(); i++) {
                json = api.getData(DEFAULT_ADDRESS + seriesName + "&season=" + i + "&apikey=" + API_KEY);
                SeasonData seasonData = converter.convertData(json, SeasonData.class);
                seasons.add(seasonData);
            }

            List<EpisodeData> episodeData = seasons.stream()
                    .flatMap(s -> s.episodes().stream())
                    .toList();

            List<Episode> episodes = seasons.stream()
                    .flatMap(s -> s.episodes().stream()
                            .map(d -> new Episode(s.seasonNumber(), d))
                    ).toList();
            while (true) {
                System.out.println("\nOptions: " +
                        "\n [0] Exit " +
                        "\n [1] Show all episodes names " +
                        "\n [2] Top 5 episodes " +
                        "\n [3] All episodes info " +
                        "\n [4] Choose episodes from a year onwards" +
                        "\n [5] Search a episode " +
                        "\n [6] Average per season " +
                        "\n [7] General statistics");
                try {
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
                        case 4:
                            System.out.println("Choose a year onwards: ");
                            var year = scanner.nextInt();
                            scanner.nextLine();

                            LocalDate searchDate = LocalDate.of(year, 1, 1);

                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

                            episodes.stream()
                                    .filter(e -> e.getReleaseDate() != null && e.getReleaseDate().isAfter(searchDate))
                                    .forEach(e -> System.out.println(
                                            "Season: " + e.getSeason() +
                                            " Episode: " + e.getEpisodeNumber() +
                                            " Released: " + e.getReleaseDate().format(formatter)
                                    ));
                            break;
                        case 5:
                            System.out.println("Search a episode: ");
                            var search = scanner.nextLine();
//                            Optional<Episode> searched = episodes.stream()
//                                    .filter(e -> e.getTitle().toLowerCase().contains(search.toLowerCase()))
//                                    .findFirst();
//                            System.out.println(searched);
                            episodes.stream()
                                    .filter(e -> e.getTitle().toLowerCase().contains(search.toLowerCase()))
                                    .forEach(System.out::println);
                            break;
                        case 6:
                            Map<Integer, Double> ratingPerSeason = episodes.stream()
                                    .filter(e -> e.getEpisodeRating() > 0.0)
                                    .collect(Collectors.groupingBy(Episode::getSeason,
                                            Collectors.averagingDouble(Episode::getEpisodeRating)));
                            System.out.println(ratingPerSeason);
                            break;
                        case 7:
                            DoubleSummaryStatistics est = episodes.stream()
                                    .filter(e -> e.getEpisodeRating() > 0.0)
                                    .collect(Collectors.summarizingDouble(Episode::getEpisodeRating));
                            System.out.println("Number of episodes: " + est.getCount() +
                                                "\nAverage rating " + est.getAverage() +
                                                "\nMax rating episode " + est.getMax() +
                                                "\nMin rating episode " + est.getMin());
                            break;
                    }
                    if (choice == 0) {
                        break;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid number");
                }
            }
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }
    }
}
