package com.example.ecommerce_project.repositories;

import com.example.ecommerce_project.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment,Long> {
}
