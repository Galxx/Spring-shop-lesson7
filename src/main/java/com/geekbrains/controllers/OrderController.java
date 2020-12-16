package com.geekbrains.controllers;

import com.geekbrains.entities.Order;
import com.geekbrains.repositories.OrderRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/order")
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;

    @GetMapping
    public List<Order> findAllOrders() {
        List<Order> orders = orderRepository.findAll();

        return orders;
    }

}
