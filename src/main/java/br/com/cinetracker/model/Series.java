package br.com.cinetracker.model;

import br.com.cinetracker.service.GPTService;
import com.fasterxml.jackson.annotation.JsonAlias;

import java.util.OptionalDouble;

public class Series {
    private String title;
    private Integer totalSeasons;
    private Double rating;
    private Category genre;
    private String actors;
    private String poster;
    private String plot;

    public Series(SeriesData d) {
        this.title = d.title();
        this.totalSeasons = d.totalSeasons();
        this.rating = OptionalDouble.of(Double.parseDouble(d.rating())).orElse(0);
        this.genre = Category.fromString(d.genre().split(",")[0].strip());
        this.actors = d.actors();
        this.poster = d.poster();
        this.plot = GPTService.getTranslate(d.plot()).trim();
    }

    public String getTitle() {
        return title;
    }

    public Integer getTotalSeasons() {
        return totalSeasons;
    }

    public Double getRating() {
        return rating;
    }

    public Category getGenre() {
        return genre;
    }

    public String getActors() {
        return actors;
    }

    public String getPoster() {
        return poster;
    }

    public String getPlot() {
        return plot;
    }

    @Override
    public String toString() {
        return "title= " + title +
                " totalSeasons= " + totalSeasons +
                " rating= " + rating +
                " genre= " + genre +
                " actors= " + actors +
                " poster= " + poster +
                " plot= " + plot;
    }
}
