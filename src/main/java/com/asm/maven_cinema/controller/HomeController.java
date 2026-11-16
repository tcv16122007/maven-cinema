package com.asm.maven_cinema.controller;

import com.asm.maven_cinema.enums.MovieStatus;
import com.asm.maven_cinema.repository.MovieRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final MovieRepository movieRepository;

    public HomeController(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("nowShowing", movieRepository.findByStatus(MovieStatus.NOW_SHOWING));
        model.addAttribute("comingSoon", movieRepository.findByStatus(MovieStatus.COMING_SOON));
        return "customer/home";
    }
}