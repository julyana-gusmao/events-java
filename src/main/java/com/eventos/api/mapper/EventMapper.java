package com.eventos.api.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.eventos.api.domain.address.Address;
import com.eventos.api.domain.event.Event;
import com.eventos.api.dto.address.AddressResponseDTO;
import com.eventos.api.dto.coupons.CouponResponseDTO;
import com.eventos.api.dto.event.EventDetailsDTO;
import com.eventos.api.dto.event.EventRequestDTO;
import com.eventos.api.dto.event.EventResponseDTO;
import com.eventos.api.dto.event.EventUpdateDTO;

@Mapper(componentModel = "spring")
public interface EventMapper {

    Event toEntity(EventRequestDTO dto);

    @Mapping(source = "address", target = "address")
    EventResponseDTO toDTO(Event event);

    List<EventResponseDTO> toDTOList(List<Event> events);

    @Mapping(source = "event.id", target = "id")
    @Mapping(source = "event.title", target = "title")
    @Mapping(source = "event.description", target = "description")
    @Mapping(source = "event.date", target = "date")
    @Mapping(source = "event.imgUrl", target = "imgUrl")
    @Mapping(source = "event.eventUrl", target = "eventUrl")
    @Mapping(source = "coupons", target = "coupons")
    @Mapping(source = "event.address.city", target = "city")
    @Mapping(source = "event.address.uf", target = "uf")
    EventDetailsDTO toDetailsDTO(Event event, List<CouponResponseDTO> coupons);

    void updateEventFromDto(EventUpdateDTO dto, @MappingTarget Event event);

    void updateAddressFromDto(EventUpdateDTO dto, @MappingTarget Address address);

    AddressResponseDTO toAddressDTO(Address address);
}
