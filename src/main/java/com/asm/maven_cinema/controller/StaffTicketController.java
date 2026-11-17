package com.asm.maven_cinema.controller;

import com.asm.maven_cinema.entity.Ticket;
import com.asm.maven_cinema.enums.TicketStatus;
import com.asm.maven_cinema.repository.TicketRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/staff")
public class StaffTicketController {

    private final TicketRepository ticketRepository;

    public StaffTicketController(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    @GetMapping({"/", "/tickets"})
    public String listTickets(@RequestParam(required = false) String keyword, Model model) {
        if (keyword != null && !keyword.isBlank()) {
            model.addAttribute("tickets", ticketRepository.searchTickets(keyword));
            model.addAttribute("keyword", keyword);
        } else {
            model.addAttribute("tickets", ticketRepository.findAllByOrderByBookingDateDesc());
        }
        return "staff/tickets";
    }

    // Check-in vé
    @PostMapping("/tickets/checkin/{id}")
    public String checkin(@PathVariable Long id, RedirectAttributes ra) {
        Ticket t = ticketRepository.findById(id).orElse(null);
        if (t != null && t.getStatus() == TicketStatus.PAID) {
            t.setStatus(TicketStatus.CHECKED_IN);
            ticketRepository.save(t);
            ra.addFlashAttribute("success", "Check-in thành công vé #" + id);
        } else {
            ra.addFlashAttribute("error", "Vé không hợp lệ hoặc đã được check-in trước đó!");
        }
        return "redirect:/staff/tickets";
    }

    // Nhân viên hủy vé (khi khách báo bận, trả vé)
    @PostMapping("/tickets/cancel/{id}")
    public String cancelTicket(@PathVariable Long id, RedirectAttributes ra) {
        Ticket t = ticketRepository.findById(id).orElse(null);
        if (t != null && t.getStatus() == TicketStatus.PAID) {
            t.setStatus(TicketStatus.CANCELLED);
            ticketRepository.save(t);
            ra.addFlashAttribute("success", "Đã hủy vé #" + id + " thành công. Ghế đã được giải phóng!");
        } else {
            ra.addFlashAttribute("error", "Không thể hủy vé này!");
        }
        return "redirect:/staff/tickets";
    }
}