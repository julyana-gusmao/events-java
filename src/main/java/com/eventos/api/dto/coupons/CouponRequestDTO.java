package com.eventos.api.dto.coupons;

import java.time.LocalDateTime;

public record CouponRequestDTO(String code, Integer discount, LocalDateTime valid) {

}
