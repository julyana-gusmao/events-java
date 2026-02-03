package com.eventos.api.mapper;

import java.util.List;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.eventos.api.domain.coupon.Coupon;
import com.eventos.api.dto.coupons.CouponRequestDTO;
import com.eventos.api.dto.coupons.CouponResponseDTO;
import com.eventos.api.dto.coupons.CouponUpdateDTO;

@Mapper(componentModel = "spring")
public interface CouponMapper {

    @Mapping(target = "event", ignore = true)
    Coupon toEntity(CouponRequestDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateCouponFromDto(CouponUpdateDTO dto, @MappingTarget Coupon entity);

    @Mapping(target = "event", source = "event")
    CouponResponseDTO toResponseDTO(Coupon coupon);

    List<CouponResponseDTO> toResponseDTOList(List<Coupon> coupons);
}
