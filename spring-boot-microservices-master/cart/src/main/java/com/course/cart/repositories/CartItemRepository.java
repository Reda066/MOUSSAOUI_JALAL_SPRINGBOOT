package com.course.cart.repositories;

import com.course.cart.domain.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<Cart, Long> {
}
