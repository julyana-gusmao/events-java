package com.eventos.api.dto.coupons;

import java.time.LocalDateTime;
import java.util.UUID;

import com.eventos.api.dto.event.EventSummaryDTO;

public record CouponResponseDTO(
        UUID id,
        String code,
        Integer discount,
        LocalDateTime valid,
        EventSummaryDTO event) {
}
