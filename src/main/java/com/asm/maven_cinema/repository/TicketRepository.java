package com.asm.maven_cinema.repository;

import com.asm.maven_cinema.entity.Ticket;
import com.asm.maven_cinema.enums.TicketStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByUserIdOrderByBookingDateDesc(Long userId);
    List<Ticket> findByShowtimeId(Long showtimeId);
    List<Ticket> findByShowtimeIdAndStatusNot(Long showtimeId, TicketStatus status);
    boolean existsByShowtimeIdAndSeatNumberAndStatusNot(Long showtimeId, String seatNumber, TicketStatus status);
    List<Ticket> findAllByOrderByBookingDateDesc();
    long countByStatus(TicketStatus status);

    @Query("SELECT COALESCE(SUM(t.totalPrice), 0) FROM Ticket t WHERE t.status = :status")
    Double sumRevenueByStatus(@Param("status") TicketStatus status);
}