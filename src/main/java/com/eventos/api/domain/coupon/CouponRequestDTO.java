package com.eventos.api.domain.coupon;

import java.time.LocalDateTime;

public record CouponRequestDTO(String code, Integer discount, LocalDateTime valid) {

}
