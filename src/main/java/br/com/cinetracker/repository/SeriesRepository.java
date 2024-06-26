package br.com.cinetracker.repository;

import br.com.cinetracker.model.Series;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeriesRepository extends JpaRepository<Series, Long> {
}
