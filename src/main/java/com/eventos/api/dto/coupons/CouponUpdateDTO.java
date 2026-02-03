package com.eventos.api.dto.coupons;

import java.time.LocalDateTime;

public record CouponUpdateDTO(
        String code,
        Integer discount,
        LocalDateTime valid) {
}
