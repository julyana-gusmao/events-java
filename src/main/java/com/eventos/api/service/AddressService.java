package com.eventos.api.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eventos.api.domain.address.Address;
import com.eventos.api.domain.event.Event;
import com.eventos.api.domain.event.EventRequestDTO;
import com.eventos.api.exception.AddressNotFoundException;
import com.eventos.api.exception.EventNotFoundException;
import com.eventos.api.repositories.AddressRepository;

@Service
public class AddressService {

    @Autowired
    private AddressRepository addressRepository;

    public Address createAddress(EventRequestDTO data, Event event) {
        Address address = new Address();
        address.setCity(data.city());
        address.setUf(data.uf());
        address.setEvent(event);

        return addressRepository.save(address);
    }

    public Address getByEvent(UUID eventId) {
        return addressRepository.findByEventId(eventId)
                .orElseThrow(() -> new EventNotFoundException("Event with id " + eventId + " not found"));
    }

    public Address updateAddress(UUID addressId, Address data) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new AddressNotFoundException("Address with id " + addressId + " not found"));

        address.setCity(data.getCity());
        address.setUf(data.getUf());

        return addressRepository.save(address);
    }

    public void deleteAddress(UUID addressId) {
        addressRepository.deleteById(addressId);
    }

}
