package com.eventos.api.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eventos.api.domain.coupon.Coupon;
import com.eventos.api.domain.coupon.CouponRequestDTO;
import com.eventos.api.domain.event.Event;
import com.eventos.api.exception.CouponNotFoundException;
import com.eventos.api.exception.EventNotFoundException;
import com.eventos.api.repositories.CouponRepository;
import com.eventos.api.repositories.EventRepository;

@Service
public class CouponService {

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private EventRepository eventRepository;

    public List<Coupon> consultCoupons(UUID eventId, LocalDateTime currentDate) {
        return couponRepository.findByEventIdAndValidAfter(eventId, currentDate);
    }

    public Coupon addCouponToEvent(UUID eventId, CouponRequestDTO couponData) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Event with id " + eventId + " not found"));

        Coupon coupon = new Coupon();
        coupon.setCode(couponData.code());
        coupon.setDiscount(couponData.discount());
        coupon.setValid(couponData.valid());
        coupon.setEvent(event);

        return couponRepository.save(coupon);
    }

    public Coupon updateCoupon(UUID couponId, CouponRequestDTO data) {
        Coupon coupon = couponRepository.findById(couponId)
                .orElseThrow(() -> new CouponNotFoundException("Coupon with id " + couponId + " not found"));

        coupon.setCode(data.code());
        coupon.setDiscount(data.discount());
        coupon.setValid(data.valid());

        return couponRepository.save(coupon);
    }

    public void deleteCoupon(UUID couponId) {
        couponRepository.deleteById(couponId);
    }

}
