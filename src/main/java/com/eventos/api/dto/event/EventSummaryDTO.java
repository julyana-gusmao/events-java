package com.eventos.api.dto.event;

import java.util.UUID;

public record EventSummaryDTO(
        UUID id,
        String title,
        Boolean remote) {
}
