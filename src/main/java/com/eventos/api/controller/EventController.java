package com.eventos.api.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.eventos.api.domain.event.Event;
import com.eventos.api.dto.coupons.CouponResponseDTO;
import com.eventos.api.dto.event.EventDetailsDTO;
import com.eventos.api.dto.event.EventRequestDTO;
import com.eventos.api.dto.event.EventResponseDTO;
import com.eventos.api.dto.event.EventUpdateDTO;
import com.eventos.api.mapper.EventMapper;
import com.eventos.api.service.CouponService;
import com.eventos.api.service.EventService;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@RequestMapping("/api/v1/events")
public class EventController {

    @Autowired
    private EventService eventService;

    @Autowired
    private CouponService couponService;

    @Autowired
    private EventMapper eventMapper;

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<EventResponseDTO> create(@ModelAttribute EventRequestDTO dto) {
        Event event = eventService.createEvent(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(eventMapper.toDTO(event));
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventDetailsDTO> getEventDetails(@PathVariable UUID eventId) {
        EventDetailsDTO eventDetails = eventService.getEventDetails(eventId);
        return ResponseEntity.ok(eventDetails);
    }

    @GetMapping()
    public ResponseEntity<List<EventResponseDTO>> getEvents(
            @RequestParam(defaultValue = "0") int _page,
            @RequestParam(defaultValue = "10") int _limit,
            @RequestParam(required = false) @Parameter(description = "Filtra por status: 'upcoming' ou 'past'", example = "upcoming") String status,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String state,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) @Parameter(description = "Filtra por data inicial (ISO 8601)", example = "2026-02-01T00:00:00") LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) @Parameter(description = "Filtra por data final (ISO 8601)", example = "2026-03-01T23:59:59") LocalDateTime endDate) {

        List<EventResponseDTO> events = eventService.getFilteredEvents(
                _page, _limit, status, title, city, state, startDate, endDate);

        return ResponseEntity.ok(events);
    }

    @GetMapping("/{eventId}/coupons")
    public ResponseEntity<List<CouponResponseDTO>> getEventCoupons(@PathVariable UUID eventId) {
        List<CouponResponseDTO> coupons = couponService.consultCoupons(eventId, LocalDateTime.now());
        return ResponseEntity.ok(coupons);
    }

    @PatchMapping("/{eventId}")
    public ResponseEntity<EventResponseDTO> updateEvent(@PathVariable UUID eventId,
            @RequestBody EventUpdateDTO data) {
        Event updated = eventService.updateEvent(eventId, data);
        return ResponseEntity.ok(eventMapper.toDTO(updated));
    }

    @DeleteMapping("/{eventId}")
    public ResponseEntity<Void> deleteEvent(@PathVariable UUID eventId) {
        eventService.deleteEvent(eventId);
        return ResponseEntity.noContent().build();
    }
}
