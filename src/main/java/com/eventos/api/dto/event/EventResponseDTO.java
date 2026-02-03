package com.eventos.api.dto.event;

import java.time.LocalDateTime;
import java.util.UUID;

import com.eventos.api.dto.address.AddressResponseDTO;

public record EventResponseDTO(
                UUID id,
                String title,
                String description,
                LocalDateTime date,
                AddressResponseDTO address,
                Boolean remote,
                String eventUrl,
                String imgUrl) {
}
