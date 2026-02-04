package com.eventos.api.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eventos.api.dto.address.AddressResponseDTO;
import com.eventos.api.dto.address.AddressUpdateDTO;
import com.eventos.api.service.AddressService;

@RestController
@RequestMapping("/api/v1/address")
public class AddressController {

    @Autowired
    private AddressService addressService;

    @GetMapping("/{eventId}")
    public ResponseEntity<AddressResponseDTO> getAddressByEvent(
            @PathVariable UUID eventId) {

        return ResponseEntity.ok(addressService.getByEvent(eventId));
    }

    @PatchMapping("/{addressId}")
    public ResponseEntity<AddressResponseDTO> updateAddress(
            @PathVariable UUID addressId,
            @RequestBody AddressUpdateDTO data) {

        return ResponseEntity.ok(addressService.updateAddress(addressId, data));
    }

    @DeleteMapping("/{addressId}")
    public ResponseEntity<Void> deleteAddress(@PathVariable UUID addressId) {
        addressService.deleteAddress(addressId);
        return ResponseEntity.noContent().build();
    }
}
