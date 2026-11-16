package com.asm.maven_cinema.controller.admin;

import com.asm.maven_cinema.enums.TicketStatus;
import com.asm.maven_cinema.repository.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminDashboardController {

    private final MovieRepository movieRepo;
    private final UserRepository userRepo;
    private final TicketRepository ticketRepo;
    private final RoomRepository roomRepo;

    public AdminDashboardController(MovieRepository movieRepo, UserRepository userRepo,
                                    TicketRepository ticketRepo, RoomRepository roomRepo) {
        this.movieRepo = movieRepo;
        this.userRepo = userRepo;
        this.ticketRepo = ticketRepo;
        this.roomRepo = roomRepo;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("totalMovies", movieRepo.count());
        model.addAttribute("totalUsers", userRepo.count());
        model.addAttribute("totalRooms", roomRepo.count());
        model.addAttribute("totalTickets", ticketRepo.count());
        Double revenue = ticketRepo.sumRevenueByStatus(TicketStatus.PAID);
        model.addAttribute("totalRevenue", revenue != null ? revenue : 0.0);
        model.addAttribute("recentTickets",
                ticketRepo.findAllByOrderByBookingDateDesc().stream().limit(10).toList());
        return "admin/dashboard";
    }
}