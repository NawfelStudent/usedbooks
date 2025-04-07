package com.bookstore.usedbooks.repository;

import com.bookstore.usedbooks.model.Order;
import com.bookstore.usedbooks.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Page<Order> findByBuyer(User buyer, Pageable pageable);
    Optional<Order> findByOrderNumber(String orderNumber);
}

