package com.eventos.api.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.eventos.api.domain.coupon.Coupon;
import com.eventos.api.domain.event.Event;
import com.eventos.api.dto.coupons.CouponResponseDTO;
import com.eventos.api.dto.event.EventSummaryDTO;

@Mapper(componentModel = "spring")
public interface CouponMapper {

    @Mapping(target = "event", source = "event")
    CouponResponseDTO toResponseDTO(Coupon coupon);

    EventSummaryDTO toEventSummaryDTO(Event event);

    List<CouponResponseDTO> toDTOList(List<Coupon> coupons);
}
