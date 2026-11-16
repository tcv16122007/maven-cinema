package com.asm.maven_cinema.repository;

import com.asm.maven_cinema.entity.Movie;
import com.asm.maven_cinema.enums.MovieStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Long> {
    List<Movie> findByStatus(MovieStatus status);
    List<Movie> findByTitleContainingIgnoreCase(String title);
    List<Movie> findByCategoryId(Long categoryId);
    List<Movie> findByTitleContainingIgnoreCaseAndStatus(String title, MovieStatus status);
    long countByStatus(MovieStatus status);
}