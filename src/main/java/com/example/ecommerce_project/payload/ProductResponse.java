package com.example.ecommerce_project.payload;

import com.example.ecommerce_project.model.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {
    List<ProductDTO> products;

}
