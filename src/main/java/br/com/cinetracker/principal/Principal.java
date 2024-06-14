package br.com.cinetracker.principal;

import br.com.cinetracker.models.SeasonData;
import br.com.cinetracker.models.SeriesData;
import br.com.cinetracker.services.ApiService;
import br.com.cinetracker.services.DataConverter;

import java.util.*;

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

        System.out.println("\nOptions: " +
                "\n [1] Search Series " +
                "\n [2] Show all episodes names " +
                "\n [0] Exit ");

        try {
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    searchSeries();
                    break;
                case 2:
                    searchEpisodeBySeries();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Invalid choice");
            }
        } catch (InputMismatchException e) {
            System.out.println(e.getMessage());
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }

    }

    private SeriesData getDataSeries() {
        System.out.println("Enter series name");
        var seriesName = scanner.nextLine();
        var json = api.getData(DEFAULT_ADDRESS + seriesName.replace(" ", "+") + "&apikey=" + API_KEY);
        return converter.convertData(json, SeriesData.class);
    }

    private void searchSeries() {
        SeriesData data = getDataSeries();
        System.out.println(data);
    }

    private void searchEpisodeBySeries() {
        SeriesData seriesData = getDataSeries();
        List<SeasonData> seasons = new ArrayList<>();

        for (int i = 1; i <= seriesData.totalSeasons(); i++) {
            var json = api.getData(DEFAULT_ADDRESS + seriesData.title().replace(" ", "+")+ "&season=" + i + "&apikey=" + API_KEY);
            SeasonData seasonData = converter.convertData(json, SeasonData.class);
            seasons.add(seasonData);
        }
        seasons.forEach(System.out::println);
    }
}