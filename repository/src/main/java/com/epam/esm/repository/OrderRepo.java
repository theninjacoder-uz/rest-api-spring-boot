package com.epam.esm.repository;

import com.epam.esm.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepo extends JpaRepository<Long, Order> {
}
