package com.asm.maven_cinema.controller;

import com.asm.maven_cinema.entity.*;
import com.asm.maven_cinema.enums.TicketStatus;
import com.asm.maven_cinema.repository.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
public class BookingController {

    private final ShowtimeRepository showtimeRepository;
    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;

    public BookingController(ShowtimeRepository showtimeRepository,
                             TicketRepository ticketRepository,
                             UserRepository userRepository) {
        this.showtimeRepository = showtimeRepository;
        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/booking/{showtimeId}")
    public String seatSelection(@PathVariable Long showtimeId, Model model) {
        Showtime st = showtimeRepository.findById(showtimeId).orElse(null);
        if (st == null) return "redirect:/movies";

        int capacity = st.getRoom().getCapacity();
        int cols = 8;
        int rows = (int) Math.ceil((double) capacity / cols);
        List<String> seats = new ArrayList<>();
        for (int r = 0; r < rows; r++) {
            char rowChar = (char) ('A' + r);
            for (int c = 1; c <= cols; c++) {
                if (seats.size() < capacity) {
                    seats.add(rowChar + String.valueOf(c));
                }
            }
        }

        // Đã sửa warning Null-safety ở đây (dùng lambda thay vì method reference)
        List<String> bookedSeats = ticketRepository
                .findByShowtimeIdAndStatusNot(showtimeId, TicketStatus.CANCELLED)
                .stream().map(t -> t.getSeatNumber()).toList();

        model.addAttribute("showtime", st);
        model.addAttribute("seats", seats);
        model.addAttribute("bookedSeats", bookedSeats);
        model.addAttribute("cols", cols);
        return "customer/booking";
    }

    @PostMapping("/booking")
    public String confirmBooking(@RequestParam Long showtimeId,
                                 @RequestParam String seats,
                                 Authentication auth,
                                 RedirectAttributes ra) {
        Showtime st = showtimeRepository.findById(showtimeId).orElse(null);
        User user = userRepository.findByUsername(auth.getName()).orElse(null);
        if (st == null || user == null) {
            ra.addFlashAttribute("error", "Không tìm thấy suất chiếu hoặc người dùng");
            return "redirect:/movies";
        }

        String[] seatArr = seats.split(",");
        double total = st.getPrice() * seatArr.length;

        for (String seat : seatArr) {
            if (ticketRepository.existsByShowtimeIdAndSeatNumberAndStatusNot(
                    showtimeId, seat.trim(), TicketStatus.CANCELLED)) {
                ra.addFlashAttribute("error", "Ghế " + seat + " đã có người đặt");
                return "redirect:/booking/" + showtimeId;
            }
        }

        for (String seat : seatArr) {
            Ticket t = Ticket.builder()
                    .user(user)
                    .showtime(st)
                    .seatNumber(seat.trim())
                    .totalPrice(st.getPrice())
                    .status(TicketStatus.PAID)
                    .build();
            ticketRepository.save(t);
        }

        ra.addFlashAttribute("success", "Đặt vé thành công! Tổng tiền: " + String.format("%,.0f", total) + "đ");
        return "redirect:/bookings";
    }

    @GetMapping("/bookings")
    public String history(Authentication auth, Model model) {
        User user = userRepository.findByUsername(auth.getName()).orElse(null);
        if (user != null) {
            model.addAttribute("tickets", ticketRepository.findByUserIdOrderByBookingDateDesc(user.getId()));
        }
        return "customer/history";
    }

    @PostMapping("/bookings/cancel/{id}")
    public String cancel(@PathVariable Long id, Authentication auth, RedirectAttributes ra) {
        Ticket t = ticketRepository.findById(id).orElse(null);
        User user = userRepository.findByUsername(auth.getName()).orElse(null);
        if (t != null && user != null && t.getUser().getId().equals(user.getId())
                && t.getStatus() == TicketStatus.PAID) {
            t.setStatus(TicketStatus.CANCELLED);
            ticketRepository.save(t);
            ra.addFlashAttribute("success", "Đã hủy vé thành công");
        } else {
            ra.addFlashAttribute("error", "Không thể hủy vé này");
        }
        return "redirect:/bookings";
    }
}