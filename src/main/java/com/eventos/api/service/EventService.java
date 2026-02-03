package com.eventos.api.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.eventos.api.domain.address.Address;
import com.eventos.api.domain.event.Event;
import com.eventos.api.dto.coupons.CouponResponseDTO;
import com.eventos.api.dto.event.EventDetailsDTO;
import com.eventos.api.dto.event.EventRequestDTO;
import com.eventos.api.dto.event.EventResponseDTO;
import com.eventos.api.dto.event.EventUpdateDTO;
import com.eventos.api.exception.EventNotFoundException;
import com.eventos.api.mapper.EventMapper;
import com.eventos.api.repositories.EventRepository;

@Service
public class EventService {

    @Autowired
    private AddressService addressService;

    @Autowired
    private CouponService couponService;

    @Autowired
    private EventRepository repository;

    @Autowired
    private EventMapper eventMapper;

    @Value("${app.upload.dir:uploads}")
    private String uploadDir;

    public Event createEvent(EventRequestDTO data) {
        Event newEvent = eventMapper.toEntity(data);

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

        Page<Event> eventsPage = repository.findUpcomingEvents(LocalDateTime.now(), pageable);

        return eventMapper.toDTOList(eventsPage.getContent());
    }

    public List<EventResponseDTO> getFilteredEvents(int page, int size, String title, String city, String uf,
            LocalDateTime startDate, LocalDateTime endDate) {

        title = (title != null) ? title : "";
        city = (city != null) ? city : "";
        uf = (uf != null) ? uf : "";
        startDate = (startDate != null) ? startDate : LocalDateTime.now();
        endDate = (endDate != null) ? endDate : LocalDateTime.now();

        Pageable pageable = PageRequest.of(page, size);

        Page<Event> eventsPage = repository.findFilteredEvents(title, city, uf, startDate, endDate, pageable);

        return eventMapper.toDTOList(eventsPage.getContent());
    }

    public EventDetailsDTO getEventDetails(UUID eventId) {
        Event event = repository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Event with id " + eventId + " not found"));

        List<CouponResponseDTO> couponDTOs = couponService.consultCoupons(eventId, LocalDateTime.now());

        return eventMapper.toDetailsDTO(event, couponDTOs);
    }

    public Event updateEvent(UUID eventId, EventUpdateDTO dto) {
        Event event = repository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Event with id " + eventId + " not found"));

        eventMapper.updateEventFromDto(dto, event);

        if (dto.city() != null || dto.uf() != null) {

            Address address = event.getAddress();

            if (address == null) {
                address = new Address();
                address.setEvent(event);
                event.setAddress(address);
            }

            eventMapper.updateAddressFromDto(dto, address);
        }

        if (Boolean.TRUE.equals(dto.remote())) {
            event.setAddress(null);
        }

        Event saved = repository.save(event);
        return repository.findById(saved.getId()).orElseThrow();
    }

    public void deleteEvent(UUID eventId) {
        Event event = repository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Event with id " + eventId + " not found"));
        repository.delete(event);
    }

    public List<EventResponseDTO> getAllEvents(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("date").descending());
        Page<Event> eventsPage = repository.findAll(pageable);

        return eventMapper.toDTOList(eventsPage.getContent());
    }

    public List<CouponResponseDTO> getEventCoupons(UUID eventId) {
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
