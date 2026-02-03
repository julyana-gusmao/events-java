package com.eventos.api.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.eventos.api.domain.coupon.Coupon;
import com.eventos.api.domain.event.Event;
import com.eventos.api.domain.event.EventDetailsDTO;
import com.eventos.api.domain.event.EventRequestDTO;
import com.eventos.api.domain.event.EventResponseDTO;
import com.eventos.api.exception.EventNotFoundException;
import com.eventos.api.repositories.EventRepository;

@Service
public class EventService {

    @Autowired
    private AddressService addressService;

    @Autowired
    private CouponService couponService;

    @Autowired
    private EventRepository repository;

    @Value("${app.upload.dir:uploads}")
    private String uploadDir;

    public Event createEvent(EventRequestDTO data) {
        Event newEvent = new Event();
        newEvent.setTitle(data.title());
        newEvent.setDescription(data.description());
        newEvent.setEventUrl(data.eventUrl());
        newEvent.setDate(data.date());
        newEvent.setRemote(data.remote());

        if (data.image() != null && !data.image().isEmpty()) {
            String path = saveFile(data.image());
            newEvent.setImgUrl(path);
        }

        Event savedEvent = repository.save(newEvent);

        addressService.createAddress(data, savedEvent);

        return savedEvent;
    }

    public List<EventResponseDTO> getUpcomingEvents(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("date").descending());

        Page<Event> eventsPage = this.repository.findUpcomingEvents(LocalDateTime.now(), pageable);

        return eventsPage
                .map(event -> new EventResponseDTO(
                        event.getId(),
                        event.getTitle(),
                        event.getDescription(),
                        event.getDate(),
                        event.getAddress() != null ? event.getAddress().getCity() : "",
                        event.getAddress() != null ? event.getAddress().getUf() : "",
                        event.getRemote(),
                        event.getEventUrl(),
                        event.getImgUrl()))
                .getContent();
    }

    public List<EventResponseDTO> getFilteredEvents(int page, int size, String title, String city, String uf,
            LocalDateTime startDate, LocalDateTime endDate) {
        title = (title != null) ? title : "";
        city = (city != null) ? city : "";
        uf = (uf != null) ? uf : "";
        startDate = (startDate != null) ? startDate : LocalDateTime.now();
        endDate = (endDate != null) ? endDate : LocalDateTime.now();

        Pageable pageable = PageRequest.of(page, size);

        Page<Event> eventsPage = this.repository.findFilteredEvents(title, city, uf, startDate,
                endDate,
                pageable);

        return eventsPage
                .map(event -> new EventResponseDTO(
                        event.getId(),
                        event.getTitle(),
                        event.getDescription(),
                        event.getDate(),
                        event.getAddress() != null ? event.getAddress().getCity() : "",
                        event.getAddress() != null ? event.getAddress().getUf() : "",
                        event.getRemote(),
                        event.getEventUrl(),
                        event.getImgUrl()))
                .getContent();
    }

    public EventDetailsDTO getEventDetails(UUID eventId) {
        Event event = repository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Event with id " + eventId + " not found"));

        List<EventDetailsDTO.CouponDTO> couponDTOs = couponService.consultCoupons(eventId, LocalDateTime.now())
                .stream()
                .map(coupon -> new EventDetailsDTO.CouponDTO(
                        coupon.getCode(),
                        coupon.getDiscount(),
                        coupon.getValid()))
                .collect(Collectors.toList());

        return new EventDetailsDTO(
                event.getId(),
                event.getTitle(),
                event.getDescription(),
                event.getDate(),
                event.getAddress() != null ? event.getAddress().getCity() : "",
                event.getAddress() != null ? event.getAddress().getUf() : "",
                event.getImgUrl(),
                event.getEventUrl(),
                couponDTOs);
    }

    public Event updateEvent(UUID eventId, EventRequestDTO data) {
        Event event = repository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Event with id " + eventId + " not found"));

        event.setTitle(data.title());
        event.setDescription(data.description());
        event.setDate(data.date());
        event.setEventUrl(data.eventUrl());
        event.setRemote(data.remote());

        return repository.save(event);
    }

    public void deleteEvent(UUID eventId) {
        Event event = repository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Event with id " + eventId + " not found"));
        repository.delete(event);
    }

    public List<EventResponseDTO> getAllEvents(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("date").descending());
        Page<Event> eventsPage = repository.findAll(pageable);

        return eventsPage.stream().map(event -> new EventResponseDTO(
                event.getId(),
                event.getTitle(),
                event.getDescription(),
                event.getDate(),
                event.getAddress() != null ? event.getAddress().getCity() : "",
                event.getAddress() != null ? event.getAddress().getUf() : "",
                event.getRemote(),
                event.getEventUrl(),
                event.getImgUrl())).collect(Collectors.toList());
    }

    public List<Coupon> getEventCoupons(UUID eventId) {
        return couponService.consultCoupons(eventId, LocalDateTime.now());
    }

    private String saveFile(MultipartFile file) {
        try {
            File dir = new File(uploadDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            String filename = UUID.randomUUID() + "-" + Objects.requireNonNull(file.getOriginalFilename());
            File dest = new File(dir, filename);

            try (FileOutputStream fos = new FileOutputStream(dest)) {
                fos.write(file.getBytes());
            }

            return "/uploads/" + filename;

        } catch (IOException e) {
            return "Não foi possível salvar o arquivo" + e;
        }
    }
}
