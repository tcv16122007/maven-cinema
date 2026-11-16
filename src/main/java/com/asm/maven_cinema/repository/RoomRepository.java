package com.asm.maven_cinema.repository;

import com.asm.maven_cinema.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Long> {
}