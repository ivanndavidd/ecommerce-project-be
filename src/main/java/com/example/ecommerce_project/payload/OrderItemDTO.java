package com.example.ecommerce_project.payload;

import com.example.ecommerce_project.model.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemDTO {
    private String orderItemId;
    private Product product;
    private int quantity;
    private double discount;
    private double orderedProductPrice;
}
