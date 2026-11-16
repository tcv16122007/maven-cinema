package com.asm.maven_cinema.controller;

import com.asm.maven_cinema.entity.Movie;
import com.asm.maven_cinema.repository.MovieRepository;
import com.asm.maven_cinema.repository.ShowtimeRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/movies")
public class MovieController {

    private final MovieRepository movieRepository;
    private final ShowtimeRepository showtimeRepository;

    public MovieController(MovieRepository movieRepository, ShowtimeRepository showtimeRepository) {
        this.movieRepository = movieRepository;
        this.showtimeRepository = showtimeRepository;
    }

    @GetMapping
    public String list(@RequestParam(required = false) String keyword, Model model) {
        List<Movie> movies;
        if (keyword != null && !keyword.isBlank()) {
            movies = movieRepository.findByTitleContainingIgnoreCase(keyword);
        } else {
            movies = movieRepository.findAll();
        }
        model.addAttribute("movies", movies);
        model.addAttribute("keyword", keyword);
        return "customer/list";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        Movie movie = movieRepository.findById(id).orElse(null);
        if (movie == null) return "redirect:/movies";
        
        model.addAttribute("movie", movie);
        // Lấy danh sách suất chiếu trong tương lai
        model.addAttribute("showtimes", 
            showtimeRepository.findByMovieIdAndStartTimeAfterOrderByStartTimeAsc(id, LocalDateTime.now()));
        return "customer/detail";
    }
}