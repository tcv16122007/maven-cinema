package com.asm.maven_cinema.controller;

import com.asm.maven_cinema.entity.Ticket;
import com.asm.maven_cinema.enums.TicketStatus;
import com.asm.maven_cinema.repository.TicketRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping({"/staff", "/admin"}) // Map cho cả 2 đường dẫn
public class StaffTicketController {

    private final TicketRepository ticketRepository;

    public StaffTicketController(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    @GetMapping("/tickets")
    public String listTickets(Model model) {
        model.addAttribute("tickets", ticketRepository.findAllByOrderByBookingDateDesc());
        return "staff/tickets";
    }

    @PostMapping("/tickets/checkin/{id}")
    public String checkin(@PathVariable Long id, RedirectAttributes ra) {
        Ticket t = ticketRepository.findById(id).orElse(null);
        if (t != null && t.getStatus() == TicketStatus.PAID) {
            t.setStatus(TicketStatus.CHECKED_IN);
            ticketRepository.save(t);
            ra.addFlashAttribute("success", "Check-in thành công vé #" + id);
        } else {
            ra.addFlashAttribute("error", "Vé không hợp lệ hoặc đã check-in rồi");
        }
        return "redirect:/staff/tickets";
    }
}