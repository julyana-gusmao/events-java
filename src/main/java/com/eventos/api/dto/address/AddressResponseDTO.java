package com.eventos.api.dto.address;

import java.util.UUID;

public record AddressResponseDTO(
                UUID id,
                String city,
                String state,
                UUID eventId) {
}
