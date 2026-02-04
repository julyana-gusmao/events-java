package com.eventos.api.repositories;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.eventos.api.domain.event.Event;

public interface EventRepository extends JpaRepository<Event, UUID> {
    @Query("""
                SELECT e
                FROM Event e
                LEFT JOIN e.address a
                WHERE (:title IS NULL OR e.title LIKE :title)
                  AND (:city IS NULL OR a.city LIKE :city)
                  AND (:state IS NULL OR a.state LIKE :state)
                  AND (:status IS NULL OR :status = ''
                       OR (:status = 'upcoming' AND e.date >= :currentDate)
                       OR (:status = 'past' AND e.date < :currentDate))
                  AND e.date >= :startDate
                  AND e.date <= :endDate
                ORDER BY e.date DESC
            """)
    Page<Event> findFilteredEvents(
            @Param("status") String status,
            @Param("title") String title,
            @Param("city") String city,
            @Param("state") String state,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            @Param("currentDate") LocalDateTime currentDate,
            Pageable pageable);

}
