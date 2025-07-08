package com.example.ecommerce_project.repositories;

import com.example.ecommerce_project.model.Address;
import com.example.ecommerce_project.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<Address,Long> {
    List<Address> user(User user);
}
