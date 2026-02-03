package com.eventos.api.mapper;

import org.mapstruct.Mapper;

import com.eventos.api.domain.address.Address;
import com.eventos.api.dto.address.AddressResponseDTO;

@Mapper(componentModel = "spring")
public interface AddressMapper {

    AddressResponseDTO toResponseDTO(Address address);
}
