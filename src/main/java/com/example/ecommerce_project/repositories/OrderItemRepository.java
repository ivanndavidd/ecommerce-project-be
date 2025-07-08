package com.example.ecommerce_project.repositories;

import com.example.ecommerce_project.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem,Long> {

}
