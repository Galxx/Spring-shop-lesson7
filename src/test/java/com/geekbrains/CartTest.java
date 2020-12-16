package com.geekbrains;

import com.geekbrains.entities.Product;
import com.geekbrains.services.CartService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

@SpringBootTest
public class CartTest {
    @Autowired
    private CartService cart;

    @Test
    public void cartFillingTest() {
        for (int i = 0; i < 5; i++) {
            Product product = new Product();
            product.setId((long) i);
            product.setPrice(new BigDecimal(100 + (long) i * 10));
            product.setTitle("Product #" + (long) i);
            cart.add(product);
        }
        Assertions.assertEquals(5, cart.getItems().size());
    }

    @Test
    public void cartClearTest() {
        for (int i = 0; i < 6; i++) {
            Product product = new Product();
            product.setId((long) i);
            product.setPrice(new BigDecimal(100 + (long) i * 10));
            product.setTitle("Product #" + (long) i);
            cart.add(product);
        }

        Assertions.assertEquals(BigDecimal.valueOf(1350), cart.getPrice());
        Assertions.assertEquals(6, cart.getItems().size());
        cart.clear();
        Assertions.assertEquals(0, cart.getItems().size());
    }
}
