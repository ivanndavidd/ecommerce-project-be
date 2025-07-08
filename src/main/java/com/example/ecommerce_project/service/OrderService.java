package com.example.ecommerce_project.service;

import com.example.ecommerce_project.payload.OrderDTO;
import jakarta.transaction.Transactional;

public interface OrderService {
    @Transactional
    OrderDTO placeOrder(String emailId, Long addressId, String paymentMethod, String pgName, String pgPaymentId, String pgStatus, String pgResponseMessage);
}
