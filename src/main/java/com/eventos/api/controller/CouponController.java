package com.eventos.api.controller;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eventos.api.domain.coupon.Coupon;
import com.eventos.api.dto.coupons.CouponRequestDTO;
import com.eventos.api.dto.coupons.CouponResponseDTO;
import com.eventos.api.dto.coupons.CouponUpdateDTO;
import com.eventos.api.mapper.CouponMapper;
import com.eventos.api.service.CouponService;

@RestController
@RequestMapping("/api/v1/coupons")
public class CouponController {
    @Autowired
    private CouponService couponService;

    @Autowired
    private CouponMapper couponMapper;

    @PostMapping("/events/{eventId}/coupons")
    public ResponseEntity<CouponResponseDTO> create(
            @PathVariable UUID eventId,
            @RequestBody CouponRequestDTO dto) {

        Coupon coupon = couponService.addCouponToEvent(eventId, dto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(couponMapper.toResponseDTO(coupon));
    }

    @PatchMapping("/{couponId}")
    public ResponseEntity<CouponResponseDTO> updateCoupon(
            @PathVariable UUID couponId,
            @RequestBody CouponUpdateDTO data) {

        return ResponseEntity.ok(couponService.updateCoupon(couponId, data));
    }

    @DeleteMapping("/{couponId}")
    public ResponseEntity<Void> deleteCoupon(@PathVariable UUID couponId) {
        couponService.deleteCoupon(couponId);
        return ResponseEntity.noContent().build();
    }
}
