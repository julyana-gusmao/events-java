package com.eventos.api.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eventos.api.domain.coupon.Coupon;
import com.eventos.api.domain.event.Event;
import com.eventos.api.dto.coupons.CouponRequestDTO;
import com.eventos.api.dto.coupons.CouponResponseDTO;
import com.eventos.api.dto.coupons.CouponUpdateDTO;
import com.eventos.api.exception.CouponNotFoundException;
import com.eventos.api.exception.EventNotFoundException;
import com.eventos.api.mapper.CouponMapper;
import com.eventos.api.repositories.CouponRepository;
import com.eventos.api.repositories.EventRepository;

@Service
public class CouponService {

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private CouponMapper couponMapper;

    public List<CouponResponseDTO> consultCoupons(UUID eventId, LocalDateTime currentDate) {
        List<Coupon> coupons = couponRepository.findByEventIdAndValidAfter(eventId, currentDate);
        return couponMapper.toResponseDTOList(coupons);
    }

    public Coupon addCouponToEvent(UUID eventId, CouponRequestDTO dto) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Event not found"));

        Coupon coupon = couponMapper.toEntity(dto);
        coupon.setEvent(event);

        return couponRepository.save(coupon); // Retorna entity
    }

    public CouponResponseDTO updateCoupon(UUID couponId, CouponUpdateDTO data) {
        Coupon coupon = couponRepository.findById(couponId)
                .orElseThrow(() -> new CouponNotFoundException("Coupon not found"));

        couponMapper.updateCouponFromDto(data, coupon);

        return couponMapper.toResponseDTO(couponRepository.save(coupon));
    }

    public void deleteCoupon(UUID couponId) {
        couponRepository.deleteById(couponId);
    }

}
