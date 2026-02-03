package com.eventos.api.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
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
import org.springframework.web.multipart.MultipartFile;

import com.eventos.api.domain.event.Event;
import com.eventos.api.dto.coupons.CouponResponseDTO;
import com.eventos.api.dto.event.EventDetailsDTO;
import com.eventos.api.dto.event.EventRequestDTO;
import com.eventos.api.dto.event.EventResponseDTO;
import com.eventos.api.dto.event.EventUpdateDTO;
import com.eventos.api.mapper.CouponMapper;
import com.eventos.api.mapper.EventMapper;
import com.eventos.api.service.CouponService;
import com.eventos.api.service.EventService;

@RestController
@RequestMapping("/api/event")
public class EventController {

    @Autowired
    private EventService eventService;

    @Autowired
    private CouponService couponService;

    @Autowired
    private EventMapper eventMapper;

    @Autowired
    private CouponMapper couponMapper;

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<Event> create(@RequestParam() String title,
            @RequestParam(required = false) String description,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date,
            @RequestParam() String city,
            @RequestParam() String uf,
            @RequestParam() Boolean remote,
            @RequestParam() String eventUrl,
            @RequestParam(required = false) MultipartFile image) {
        EventRequestDTO eventRequestDTO = new EventRequestDTO(title, description, date, city, uf, remote, eventUrl,
                image);
        Event newEvent = this.eventService.createEvent(eventRequestDTO);
        return ResponseEntity.ok(newEvent);
    }

    @GetMapping("/upcoming")
    public ResponseEntity<List<EventResponseDTO>> getUpcomingEvents(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        List<EventResponseDTO> allEvents = this.eventService.getUpcomingEvents(page, size);
        return ResponseEntity.ok(allEvents);
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventDetailsDTO> getEventDetails(@PathVariable UUID eventId) {
        EventDetailsDTO eventDetails = eventService.getEventDetails(eventId);
        return ResponseEntity.ok(eventDetails);
    }

    @GetMapping("/all")
    public ResponseEntity<List<EventResponseDTO>> getAllEvents(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        List<EventResponseDTO> events = eventService.getAllEvents(page, size);
        return ResponseEntity.ok(events);
    }

    @GetMapping("/{eventId}/coupons")
    public ResponseEntity<List<CouponResponseDTO>> getEventCoupons(@PathVariable UUID eventId) {
        List<CouponResponseDTO> coupons = couponService.consultCoupons(eventId, LocalDateTime.now());
        return ResponseEntity.ok(coupons);
    }

    @GetMapping("/filter")
    public ResponseEntity<List<EventResponseDTO>> filterEvents(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String uf,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {

        List<EventResponseDTO> events = this.eventService.getFilteredEvents(page, size, title, city, uf, startDate,
                endDate);
        return ResponseEntity.ok(events);
    }

    @PatchMapping("/{eventId}")
    public ResponseEntity<EventResponseDTO> updateEvent(
            @PathVariable UUID eventId,
            @ModelAttribute EventUpdateDTO data) {

        Event updated = eventService.updateEvent(eventId, data);
        return ResponseEntity.ok(eventMapper.toDTO(updated));
    }

    @DeleteMapping("/{eventId}")
    public ResponseEntity<Void> deleteEvent(@PathVariable UUID eventId) {
        eventService.deleteEvent(eventId);
        return ResponseEntity.noContent().build();
    }
}
