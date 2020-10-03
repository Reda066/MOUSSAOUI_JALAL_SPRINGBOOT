package com.course.order.controller;

import com.course.order.domain.OrderT;
import com.course.order.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.List;

@RestController
public class OrderController {
    
    @Autowired 
    OrderRepository orderRepository;
    
    @PostMapping(value = "/order")
    public ResponseEntity<OrderT> saveOrder(@RequestBody OrderT order) {
        orderRepository.save(order);

        if (order == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Couldn't create a new cart");

        return new ResponseEntity<OrderT>(order, HttpStatus.CREATED);
    }

    @GetMapping(value = "/orders")
    public List<OrderT> list() {
        List<OrderT> list = orderRepository.findAll();
        return list;
    }
}
