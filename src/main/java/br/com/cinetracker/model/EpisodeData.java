package br.com.cinetracker.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record EpisodeData(@JsonAlias("Title") String title,
                          @JsonAlias("Episode") Integer episode,
                          @JsonAlias("imdbRating") String episodeRating,
                          @JsonAlias("Released") String released) {
}
