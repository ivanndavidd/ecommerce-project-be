package com.example.ecommerce_project.service;

import com.example.ecommerce_project.model.User;
import com.example.ecommerce_project.payload.AddressDTO;
import jakarta.validation.Valid;

import java.util.List;

public interface AddressService {
    AddressDTO createAddress(AddressDTO addressDTO, User user);

    List<AddressDTO> getAllAddresses();

    AddressDTO getAddress(Long addressId);

    List<AddressDTO> getAddressByUser(User user);

    AddressDTO updateAddressById(Long addressId, AddressDTO addressDTO);

    String deleteAddressById(Long addressId);
}
