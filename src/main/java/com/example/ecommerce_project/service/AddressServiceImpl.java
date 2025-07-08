package com.example.ecommerce_project.service;

import com.example.ecommerce_project.exceptions.ResourceNotFoundException;
import com.example.ecommerce_project.model.Address;
import com.example.ecommerce_project.model.User;
import com.example.ecommerce_project.payload.AddressDTO;
import com.example.ecommerce_project.repositories.AddressRepository;
import com.example.ecommerce_project.repositories.UserRepository;
import com.example.ecommerce_project.utils.AuthUtil;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AddressServiceImpl implements AddressService {
    @Autowired
    ModelMapper modelMapper;

    @Autowired
    AddressRepository addressRepository;

    @Autowired
    UserRepository userRepository;

    @Override
    public AddressDTO createAddress(AddressDTO addressDTO, User user) {
        Address address = modelMapper.map(addressDTO, Address.class);
        List<Address> addressList = user.getAddresses();
        addressList.add(address);
        user.setAddresses(addressList);
        address.setUser(user);
        Address savedAddress = addressRepository.save(address);
        AddressDTO savedAddressDTO = modelMapper.map(savedAddress, AddressDTO.class);
        return savedAddressDTO;
    }

    @Override
    public List<AddressDTO> getAllAddresses() {
        List<Address> allAddress = addressRepository.findAll();
        return allAddress.stream().map(addressDTO -> modelMapper.map(addressDTO, AddressDTO.class)).collect(Collectors.toList());
    }

    @Override
    public AddressDTO getAddress(Long addressId) {
        Address addressById = addressRepository.findById(addressId).orElseThrow(()-> new ResourceNotFoundException("Address not found", "addressId", addressId));
        AddressDTO addressDTO = modelMapper.map(addressById, AddressDTO.class);
        return addressDTO;
    }

    @Override
    public List<AddressDTO> getAddressByUser(User user) {
        List<Address> addressByUser = user.getAddresses();
        return addressByUser.stream().map(addressDTO -> modelMapper.map(addressDTO, AddressDTO.class)).collect(Collectors.toList());
    }

    @Override
    public AddressDTO updateAddressById(Long addressId, AddressDTO addressDTO) {
        Address address = addressRepository.findById(addressId).orElseThrow(()-> new ResourceNotFoundException("Address not found",  "addressId", addressId));
        address.setStreet(addressDTO.getStreet());
        address.setBuilding(addressDTO.getBuilding());
        address.setCity(addressDTO.getCity());
        address.setState(addressDTO.getState());
        address.setCountry(addressDTO.getCountry());
        address.setPincode(addressDTO.getPincode());

        Address  updatedAddress = addressRepository.save(address);
        User user = address.getUser();
        user.getAddresses().removeIf(add -> add.getAddressId().equals(addressId));
        user.getAddresses().add(updatedAddress);
        userRepository.save(user);
        AddressDTO updatedAddressDTO = modelMapper.map(updatedAddress, AddressDTO.class);
        return updatedAddressDTO;
    }

    @Override
    public String deleteAddressById(Long addressId) {
        Address address = addressRepository.findById(addressId).orElseThrow(()-> new ResourceNotFoundException("Address not found", "addressId", addressId));
        User user = address.getUser();
        user.getAddresses().removeIf(add -> add.getAddressId().equals(addressId));
        userRepository.save(user);
        addressRepository.delete(address);
        return "Address deleted successfully " + addressId;
    }
}
