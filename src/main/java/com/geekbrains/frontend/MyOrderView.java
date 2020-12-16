package com.geekbrains.frontend;

import com.geekbrains.services.OrderService;
import com.vaadin.flow.router.Route;

@Route("orders")
public class MyOrderView extends OrderView {
    public MyOrderView(OrderService orderService) {
        super(orderService);
    }
}
