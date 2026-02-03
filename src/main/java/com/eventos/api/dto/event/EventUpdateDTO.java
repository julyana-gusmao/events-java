package com.eventos.api.dto.event;

import java.time.LocalDateTime;

import org.springframework.web.multipart.MultipartFile;

public record EventUpdateDTO(
        String title,
        String description,
        LocalDateTime date,
        String city,
        String uf,
        Boolean remote,
        String eventUrl,
        MultipartFile image) {
}
