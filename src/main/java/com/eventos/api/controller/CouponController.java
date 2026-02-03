package com.eventos.api.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eventos.api.dto.coupons.CouponRequestDTO;
import com.eventos.api.dto.coupons.CouponResponseDTO;
import com.eventos.api.dto.coupons.CouponUpdateDTO;
import com.eventos.api.service.CouponService;

@RestController
@RequestMapping("/api/coupon")
public class CouponController {
    @Autowired
    private CouponService couponService;

    @PostMapping("/event/{eventId}")
    public ResponseEntity<CouponResponseDTO> addCouponsToEvent(
            @PathVariable UUID eventId,
            @RequestBody CouponRequestDTO data) {

        return ResponseEntity.ok(couponService.addCouponToEvent(eventId, data));
    }

    @GetMapping("/event/{eventId}")
    public ResponseEntity<List<CouponResponseDTO>> getCouponsByEvent(
            @PathVariable UUID eventId) {

        return ResponseEntity.ok(couponService.consultCoupons(eventId, LocalDateTime.now()));
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
