package com.eventos.api.domain.event;

import java.time.LocalDateTime;

import org.springframework.web.multipart.MultipartFile;

public record EventRequestDTO(
                String title,
                String description,
                LocalDateTime date,
                String city,
                String uf,
                Boolean remote,
                String eventUrl,
                MultipartFile image) {
}
