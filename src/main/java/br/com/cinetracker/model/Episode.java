package br.com.cinetracker.model;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class Episode {
    private Integer season;
    private String title;
    private Integer episodeNumber;
    private Double episodeRating;
    private LocalDate releaseDate;


    public Episode(Integer season, EpisodeData d) {
        this.season = season;
        this.title = d.title();
        this.episodeNumber = d.episode();
        try {
            this.episodeRating = Double.valueOf(d.episodeRating());
        } catch (NumberFormatException e) {
            this.episodeRating = 0.0;
        }
        try {
            this.releaseDate = LocalDate.parse(d.released());
        } catch (DateTimeParseException e) {
            this.releaseDate = null;
        }

    }

    public Integer getSeason() {
        return season;
    }

    public String getTitle() {
        return title;
    }

    public Integer getEpisodeNumber() {
        return episodeNumber;
    }

    public Double getEpisodeRating() {
        return episodeRating;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    @Override
    public String toString() {
        return "season: " + season +
                ", title: " + title +
                ", episodeNumber: " + episodeNumber +
                ", episodeRating: " + episodeRating +
                ", releaseDate: " + releaseDate;
    }
}
