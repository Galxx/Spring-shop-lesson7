package com.geekbrains;

import com.geekbrains.Application;
import com.geekbrains.entities.Product;
import com.geekbrains.repositories.ProductRepository;
import com.geekbrains.services.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;

@DataJpaTest
@ActiveProfiles("test")
public class RepositoriesTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void productRepositoryTest() {
        Product product = new Product();
        product.setPrice(BigDecimal.TEN);
        product.setTitle("asdasd");
        Product out = entityManager.persist(product);
        entityManager.flush();

        List<Product> productsList = productRepository.findAll();

        Assertions.assertEquals(5, productsList.size());
        Assertions.assertEquals("Milk", productsList.get(1).getTitle());
    }

    @Test
    public void initDbTest() {
        List<Product> productsList = productRepository.findAll();
        Assertions.assertEquals(4, productsList.size());
    }


}
