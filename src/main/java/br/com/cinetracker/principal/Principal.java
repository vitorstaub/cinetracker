package br.com.cinetracker.principal;

import br.com.cinetracker.model.Episode;
import br.com.cinetracker.repository.SeriesRepository;
import br.com.cinetracker.model.SeasonData;
import br.com.cinetracker.model.Series;
import br.com.cinetracker.model.SeriesData;
import br.com.cinetracker.service.ApiService;
import br.com.cinetracker.service.DataConverter;

import java.util.*;

import io.github.cdimascio.dotenv.Dotenv;


public class Principal {
    private final String DEFAULT_ADDRESS = "https://www.omdbapi.com/?t=";

    private final Dotenv dotenv = Dotenv.load();
    private final String API_KEY = dotenv.get("API_KEY");

    private final ApiService api = new ApiService();
    private final Scanner scanner = new Scanner(System.in);
    private final DataConverter converter = new DataConverter();

    private final SeriesRepository repository;

    private List<Series> series = new ArrayList<>();

    public Principal(SeriesRepository repository) {
        this.repository = repository;
    }

    public void showMenu() {
        var choice = -1;
        while (choice != 0) {
            System.out.println("Enter series name");

            System.out.println("""

                    Options: \

                     [1] Search series \

                     [2] Search episodes \

                     [3] List searched series\
                    
                     [4] Search series by name\
                    
                     [5] Search series by actor\

                     [0] Exit\s""");

            try {
                choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1:
                        searchSeries();
                        break;
                    case 2:
                        searchEpisodeBySeries();
                        break;
                    case 3:
                        listSearchedSeries();
                        break;
                    case 4:
                        searchSeriesByName();
                        break;
                    case 5:
                        searchSeriesByActor();
                        break;
                    case 0:
                        break;
                    default:
                        System.out.println("Invalid choice");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid Character " + e.getMessage());
                break;
            } catch (RuntimeException e) {
                System.out.println(e.getMessage());
                break;
            }
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
        Series serie = new Series(data);
        //seriesData.add(data);
        repository.save(serie);
        System.out.println(data);
    }

    private void searchEpisodeBySeries() {
        listSearchedSeries();
        System.out.println("Enter a series name: ");
        var seriesName = scanner.nextLine();

        Optional<Series> serie = repository.findByTitleContainingIgnoreCase(seriesName);

        if (serie.isPresent()) {
            var seriesFound = serie.get();
            List<SeasonData> seasons = new ArrayList<>();

            for (int i = 1; i <= seriesFound.getTotalSeasons(); i++) {
                var json = api.getData(DEFAULT_ADDRESS + seriesFound.getTitle().replace(" ", "+")+ "&season=" + i + "&apikey=" + API_KEY);
                SeasonData seasonData = converter.convertData(json, SeasonData.class);
                seasons.add(seasonData);
            }
            seasons.forEach(System.out::println);

            List<Episode> episodes = seasons.stream()
                    .flatMap(d -> d.episodes().stream()
                            .map(e -> new Episode(e.episode(), e)))
                    .toList();

            seriesFound.setEpisodes(episodes);
            repository.save(seriesFound);
        } else  {
            System.out.println("Not Found");
        }
    }

    private void listSearchedSeries() {
        series = repository.findAll();

        series.stream()
                .sorted(Comparator.comparing(Series::getGenre))
                .forEach(System.out::println);
    }

    private void searchSeriesByName() {
        System.out.println("Enter a series name: ");
        var seriesName = scanner.nextLine();
        Optional<Series> seriesSearched = repository.findByTitleContainingIgnoreCase(seriesName);

        if (seriesSearched.isPresent()) {
            System.out.println(seriesSearched.get());
        } else {
            System.out.println("Not Found");
        }
    }

    private void searchSeriesByActor() {
        System.out.println("Enter the actor's name: ");
        var actorName = scanner.nextLine();

        System.out.println("Minimum Rating: ");
        var minRating = scanner.nextDouble();

        List<Series> seriesSearched = repository.findByActorsContainingIgnoreCaseAndRatingGreaterThanEqual(actorName, minRating);

        seriesSearched.forEach(s -> System.out.println("Title: " + s.getTitle() + " Rating: " + s.getRating()));
    }
}