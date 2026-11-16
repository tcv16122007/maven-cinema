package com.asm.maven_cinema.controller.admin;

import com.asm.maven_cinema.entity.Category;
import com.asm.maven_cinema.entity.Movie;
import com.asm.maven_cinema.enums.MovieStatus;
import com.asm.maven_cinema.repository.CategoryRepository;
import com.asm.maven_cinema.repository.MovieRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/movies")
public class AdminMovieController {

    private final MovieRepository movieRepo;
    private final CategoryRepository categoryRepo;

    public AdminMovieController(MovieRepository movieRepo, CategoryRepository categoryRepo) {
        this.movieRepo = movieRepo;
        this.categoryRepo = categoryRepo;
    }

    @GetMapping
    public String list(@RequestParam(required = false) String keyword, Model model) {
        if (keyword != null && !keyword.isBlank()) {
            model.addAttribute("movies", movieRepo.findByTitleContainingIgnoreCase(keyword));
            model.addAttribute("keyword", keyword);
        } else {
            model.addAttribute("movies", movieRepo.findAll());
        }
        return "admin/movies";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("movie", new Movie());
        model.addAttribute("categories", categoryRepo.findAll());
        model.addAttribute("statuses", MovieStatus.values());
        model.addAttribute("isEdit", false);
        return "admin/movie-form";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        Movie m = movieRepo.findById(id).orElse(null);
        if (m == null) return "redirect:/admin/movies";
        model.addAttribute("movie", m);
        model.addAttribute("categories", categoryRepo.findAll());
        model.addAttribute("statuses", MovieStatus.values());
        model.addAttribute("isEdit", true);
        return "admin/movie-form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Movie movie,
                       @RequestParam Long categoryId,
                       RedirectAttributes ra) {
        Category cat = categoryRepo.findById(categoryId).orElse(null);
        if (cat == null) {
            ra.addFlashAttribute("error", "Vui lòng chọn thể loại");
            return "redirect:/admin/movies";
        }
        movie.setCategory(cat);
        movieRepo.save(movie);
        ra.addFlashAttribute("success", movie.getId() == null
                ? "Đã thêm phim mới: " + movie.getTitle()
                : "Đã cập nhật: " + movie.getTitle());
        return "redirect:/admin/movies";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        Movie m = movieRepo.findById(id).orElse(null);
        if (m != null) {
            movieRepo.delete(m);
            ra.addFlashAttribute("success", "Đã xóa: " + m.getTitle());
        } else {
            ra.addFlashAttribute("error", "Không tìm thấy phim");
        }
        return "redirect:/admin/movies";
    }
}