package com.project.shopapp.repositories;

import com.project.shopapp.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    // tim cac don hang cuar 1 user nao dos
    List<Order> findByUserId(Long userId);

}
