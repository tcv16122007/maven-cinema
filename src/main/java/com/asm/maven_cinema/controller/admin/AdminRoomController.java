package com.asm.maven_cinema.controller.admin;

import com.asm.maven_cinema.entity.Room;
import com.asm.maven_cinema.repository.RoomRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/rooms")
public class AdminRoomController {

    private final RoomRepository roomRepo;

    public AdminRoomController(RoomRepository roomRepo) {
        this.roomRepo = roomRepo;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("rooms", roomRepo.findAll());
        return "admin/rooms";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("room", new Room());
        model.addAttribute("isEdit", false);
        return "admin/room-form";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        Room r = roomRepo.findById(id).orElse(null);
        if (r == null) return "redirect:/admin/rooms";
        model.addAttribute("room", r);
        model.addAttribute("isEdit", true);
        return "admin/room-form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Room room, RedirectAttributes ra) {
        roomRepo.save(room);
        ra.addFlashAttribute("success", "Đã lưu phòng: " + room.getName());
        return "redirect:/admin/rooms";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        try {
            roomRepo.deleteById(id);
            ra.addFlashAttribute("success", "Đã xóa phòng");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Không thể xóa phòng vì đang có suất chiếu!");
        }
        return "redirect:/admin/rooms";
    }
}