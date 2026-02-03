package com.eventos.api.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.eventos.api.domain.address.Address;
import com.eventos.api.dto.address.AddressResponseDTO;
import com.eventos.api.dto.address.AddressUpdateDTO;

@Mapper(componentModel = "spring")
public interface AddressMapper {

    @Mapping(target = "eventId", source = "event.id")
    AddressResponseDTO toResponseDto(Address address);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateAddressFromDto(AddressUpdateDTO dto, @MappingTarget Address entity);
}
