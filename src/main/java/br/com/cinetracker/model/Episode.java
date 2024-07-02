package br.com.cinetracker.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@Entity
@Table(name = "episodes")
public class Episode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private Integer season;
    private String title;
    private Integer episodeNumber;
    private Double episodeRating;
    private LocalDate releaseDate;

    @ManyToOne
    @JoinColumn(name = "series_id")
    private Series series;


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

    public Episode() {}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setSeason(Integer season) {
        this.season = season;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setEpisodeNumber(Integer episodeNumber) {
        this.episodeNumber = episodeNumber;
    }

    public void setEpisodeRating(Double episodeRating) {
        this.episodeRating = episodeRating;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Series getSeries() {
        return series;
    }

    public void setSeries(Series series) {
        this.series = series;
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
