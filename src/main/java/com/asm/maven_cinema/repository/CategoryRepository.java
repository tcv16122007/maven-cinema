package com.asm.maven_cinema.repository;

import com.asm.maven_cinema.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}