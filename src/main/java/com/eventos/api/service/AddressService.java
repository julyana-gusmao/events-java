package com.eventos.api.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eventos.api.domain.address.Address;
import com.eventos.api.domain.event.Event;
import com.eventos.api.dto.address.AddressResponseDTO;
import com.eventos.api.dto.address.AddressUpdateDTO;
import com.eventos.api.dto.event.EventRequestDTO;
import com.eventos.api.exception.AddressNotFoundException;
import com.eventos.api.exception.EventNotFoundException;
import com.eventos.api.mapper.AddressMapper;
import com.eventos.api.repositories.AddressRepository;

@Service
public class AddressService {

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private AddressMapper addressMapper;

    public Address createAddress(EventRequestDTO data, Event event) {
        Address address = new Address();
        address.setCity(data.city());
        address.setState(data.state());
        address.setEvent(event);

        return addressRepository.save(address);
    }

    public AddressResponseDTO getByEvent(UUID eventId) {
        Address address = addressRepository.findByEventId(eventId)
                .orElseThrow(() -> new EventNotFoundException(
                        "Event with id " + eventId + " not found"));

        return addressMapper.toResponseDto(address);
    }

    public AddressResponseDTO updateAddress(UUID addressId, AddressUpdateDTO data) {

        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new AddressNotFoundException("Address not found"));

        addressMapper.updateAddressFromDto(data, address);

        Address saved = addressRepository.save(address);

        return addressMapper.toResponseDto(saved);
    }

    public void deleteAddress(UUID addressId) {
        addressRepository.deleteById(addressId);
    }

}
