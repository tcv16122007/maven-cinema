package com.asm.maven_cinema.controller.admin;

import com.asm.maven_cinema.entity.Category;
import com.asm.maven_cinema.repository.CategoryRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/categories")
public class AdminCategoryController {

    private final CategoryRepository categoryRepo;

    public AdminCategoryController(CategoryRepository categoryRepo) {
        this.categoryRepo = categoryRepo;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("categories", categoryRepo.findAll());
        return "admin/categories";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("category", new Category());
        return "admin/category-form";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        Category c = categoryRepo.findById(id).orElse(null);
        if (c == null) return "redirect:/admin/categories";
        model.addAttribute("category", c);
        return "admin/category-form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Category category, RedirectAttributes ra) {
        categoryRepo.save(category);
        ra.addFlashAttribute("success", "Đã lưu thể loại: " + category.getName());
        return "redirect:/admin/categories";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        try {
            categoryRepo.deleteById(id);
            ra.addFlashAttribute("success", "Đã xóa thể loại");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Không thể xóa vì đang có phim thuộc thể loại này!");
        }
        return "redirect:/admin/categories";
    }
}