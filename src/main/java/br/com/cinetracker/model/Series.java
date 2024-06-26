package br.com.cinetracker.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;

@Entity
@Table(name = "series")
public class Series {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true)
    private String title;
    private Integer totalSeasons;
    private Double rating;
    @Enumerated(EnumType.STRING)
    private Category genre;
    private String actors;
    private String poster;
    private String plot;

    @Transient
    private List<Episode> episodes = new ArrayList<>();

    public Series(SeriesData d) {
        this.title = d.title();
        this.totalSeasons = d.totalSeasons();
        this.rating = OptionalDouble.of(Double.parseDouble(d.rating())).orElse(0);
        this.genre = Category.fromString(d.genre().split(",")[0].strip());
        this.actors = d.actors();
        this.poster = d.poster();
        this.plot = d.plot(); //GPTService.getTranslate(d.plot()).trim(); -- translate to portuguese --
    }

    public Series() {}

    public long getId() {
        return id;
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

    public void setId(long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setTotalSeasons(Integer totalSeasons) {
        this.totalSeasons = totalSeasons;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public void setGenre(Category genre) {
        this.genre = genre;
    }

    public void setActors(String actors) {
        this.actors = actors;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public void setPlot(String plot) {
        this.plot = plot;
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
