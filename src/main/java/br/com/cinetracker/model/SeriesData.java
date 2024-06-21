package br.com.cinetracker.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record SeriesData(@JsonAlias("Title") String title,
                         @JsonAlias("totalSeasons") Integer totalSeasons,
                         @JsonAlias("imdbRating") String rating) {
    public SeriesData {
        if (title == null || totalSeasons == null || rating == null) {
            throw new NullPointerException("Series data is null!");
        }
    }
}
