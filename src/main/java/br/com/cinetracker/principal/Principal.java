package br.com.cinetracker.principal;

import br.com.cinetracker.models.SeasonData;
import br.com.cinetracker.models.SeriesData;
import br.com.cinetracker.services.ApiService;
import br.com.cinetracker.services.DataConverter;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Principal {
    //https://www.omdbapi.com/?t=gilmore+girls&season=" + i + "&apikey=a997b1d7

    private final String DEFAULT_ADDRESS = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=a997b1d7";

    private ApiService api = new ApiService();
    private Scanner scanner = new Scanner(System.in);
    private DataConverter converter = new DataConverter();

    public void showMenu() {
        System.out.println("Enter series name");
        var seriesName = scanner.nextLine().replace(" ", "+").toLowerCase();

        var json = api.getData(DEFAULT_ADDRESS + seriesName + API_KEY);

        SeriesData data = converter.getData(json, SeriesData.class);
        System.out.println(data);

        List<SeasonData> seasons = new ArrayList<>();

        for (int i = 1; i <= data.totalSeasons(); i++) {
            json = api.getData(DEFAULT_ADDRESS + seriesName + "&season=" + i + API_KEY);
            SeasonData seasonData = converter.getData(json, SeasonData.class);
            seasons.add(seasonData);
        }

        seasons.forEach(System.out::println);
    }
}
