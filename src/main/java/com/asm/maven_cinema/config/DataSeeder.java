package com.asm.maven_cinema.config;

import com.asm.maven_cinema.entity.Category;
import com.asm.maven_cinema.entity.User;
import com.asm.maven_cinema.enums.Role;
import com.asm.maven_cinema.enums.UserStatus;
import com.asm.maven_cinema.repository.CategoryRepository;
import com.asm.maven_cinema.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final PasswordEncoder encoder;

    public DataSeeder(UserRepository userRepository,
                      CategoryRepository categoryRepository,
                      PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.encoder = encoder;
    }

    @Override
    public void run(String... args) {
        if (userRepository.count() > 0) return;

        // === 3 Users mặc định ===
        userRepository.saveAll(List.of(
                User.builder().username("admin").password(encoder.encode("123456"))
                        .fullName("Quản trị viên").email("admin@raven.vn")
                        .role(Role.ADMIN).status(UserStatus.ACTIVE).build(),
                User.builder().username("staff").password(encoder.encode("123456"))
                        .fullName("Nhân viên rạp").email("staff@raven.vn")
                        .role(Role.STAFF).status(UserStatus.ACTIVE).build(),
                User.builder().username("user").password(encoder.encode("123456"))
                        .fullName("Khách hàng mẫu").email("user@gmail.com")
                        .role(Role.CUSTOMER).status(UserStatus.ACTIVE).build()
        ));

        // === 5 Categories cố định (không có ảnh) ===
        categoryRepository.saveAll(List.of(
                Category.builder().name("Hành động").build(),
                Category.builder().name("Hài").build(),
                Category.builder().name("Tình cảm").build(),
                Category.builder().name("Kinh dị").build(),
                Category.builder().name("Khoa học viễn tưởng").build()
        ));

        System.out.println(">>> DataSeeder: Đã tạo 3 users (admin/staff/user) + 5 categories.");
        System.out.println(">>> Admin có thể thêm phim/phòng/suất chiếu qua /admin");
    }
}