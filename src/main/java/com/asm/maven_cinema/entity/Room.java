package com.asm.maven_cinema.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "rooms")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50, columnDefinition = "NVARCHAR(50)")
    private String name;

    @Column(nullable = false)
    private Integer capacity;
}