package com.asm.maven_cinema.controller;

import com.asm.maven_cinema.entity.User;
import com.asm.maven_cinema.enums.Role;
import com.asm.maven_cinema.enums.UserStatus;
import com.asm.maven_cinema.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    public AuthController(UserRepository userRepository, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "auth/login";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("user", new User());
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute User user, RedirectAttributes ra, Model model) {
        if (user.getUsername() == null || user.getUsername().isBlank()) {
            model.addAttribute("error", "Vui lòng nhập tên đăng nhập");
            model.addAttribute("user", user);
            return "auth/register";
        }
        if (user.getPassword() == null || user.getPassword().length() < 6) {
            model.addAttribute("error", "Mật khẩu phải từ 6 ký tự trở lên");
            model.addAttribute("user", user);
            return "auth/register";
        }
        if (userRepository.existsByUsername(user.getUsername())) {
            model.addAttribute("error", "Tên đăng nhập đã tồn tại");
            model.addAttribute("user", user);
            return "auth/register";
        }

        user.setPassword(encoder.encode(user.getPassword()));
        user.setRole(Role.CUSTOMER);
        user.setStatus(UserStatus.ACTIVE);
        userRepository.save(user);

        ra.addFlashAttribute("success", "Đăng ký thành công! Vui lòng đăng nhập.");
        return "redirect:/auth/login";
    }
}