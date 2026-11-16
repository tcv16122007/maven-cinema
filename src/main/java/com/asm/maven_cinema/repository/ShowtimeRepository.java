package com.asm.maven_cinema.repository;

import com.asm.maven_cinema.entity.Showtime;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ShowtimeRepository extends JpaRepository<Showtime, Long> {
    List<Showtime> findByMovieIdOrderByStartTimeAsc(Long movieId);
    List<Showtime> findByMovieIdAndStartTimeAfterOrderByStartTimeAsc(Long movieId, LocalDateTime time);
    List<Showtime> findAllByOrderByStartTimeDesc();
}