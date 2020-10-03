package com.course.order.repositories;

import com.course.order.domain.OrderT;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<OrderT, Long> {
}
