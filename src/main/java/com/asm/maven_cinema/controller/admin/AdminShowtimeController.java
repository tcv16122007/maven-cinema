package com.asm.maven_cinema.controller.admin;

import com.asm.maven_cinema.entity.Movie;
import com.asm.maven_cinema.entity.Room;
import com.asm.maven_cinema.entity.Showtime;
import com.asm.maven_cinema.repository.MovieRepository;
import com.asm.maven_cinema.repository.RoomRepository;
import com.asm.maven_cinema.repository.ShowtimeRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/showtimes")
public class AdminShowtimeController {

    private final ShowtimeRepository showtimeRepo;
    private final MovieRepository movieRepo;
    private final RoomRepository roomRepo;

    public AdminShowtimeController(ShowtimeRepository showtimeRepo, MovieRepository movieRepo, RoomRepository roomRepo) {
        this.showtimeRepo = showtimeRepo;
        this.movieRepo = movieRepo;
        this.roomRepo = roomRepo;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("showtimes", showtimeRepo.findAllByOrderByStartTimeDesc());
        return "admin/showtimes";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("showtime", new Showtime());
        model.addAttribute("movies", movieRepo.findAll());
        model.addAttribute("rooms", roomRepo.findAll());
        model.addAttribute("isEdit", false);
        return "admin/showtime-form";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        Showtime st = showtimeRepo.findById(id).orElse(null);
        if (st == null) return "redirect:/admin/showtimes";
        model.addAttribute("showtime", st);
        model.addAttribute("movies", movieRepo.findAll());
        model.addAttribute("rooms", roomRepo.findAll());
        model.addAttribute("isEdit", true);
        return "admin/showtime-form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Showtime showtime,
                       @RequestParam Long movieId,
                       @RequestParam Long roomId,
                       RedirectAttributes ra) {
        Movie m = movieRepo.findById(movieId).orElse(null);
        Room r = roomRepo.findById(roomId).orElse(null);
        
        if (m == null || r == null) {
            ra.addFlashAttribute("error", "Vui lòng chọn phim và phòng hợp lệ");
            return "redirect:/admin/showtimes";
        }

        showtime.setMovie(m);
        showtime.setRoom(r);
        showtimeRepo.save(showtime);
        
        ra.addFlashAttribute("success", "Đã lưu suất chiếu");
        return "redirect:/admin/showtimes";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        try {
            showtimeRepo.deleteById(id);
            ra.addFlashAttribute("success", "Đã xóa suất chiếu");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Không thể xóa vì đã có khách đặt vé!");
        }
        return "redirect:/admin/showtimes";
    }
}