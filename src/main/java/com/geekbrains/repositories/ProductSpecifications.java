package com.geekbrains.repositories;

import com.geekbrains.entities.Product;
import org.springframework.data.jpa.domain.Specification;

public class ProductSpecifications {

    public static Specification<Product> priceGreaterOrEqualsThen(int minPrice) {
        return (Specification<Product>) (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.greaterThanOrEqualTo(root.get("price"), minPrice);
    }

    public static Specification<Product> priceLesserOrEqualsThen(int maxPrice) {
        return (Specification<Product>) (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.lessThanOrEqualTo(root.get("price"), maxPrice);
    }

    public static Specification<Product> titleLike(String title) {
        return (Specification<Product>) (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.like(root.get("title"), String.format("%%%s%%", title));
    }


    public static Specification<Product> categoryIs(String category) {
        return (Specification<Product>) (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.like(root.join("categories").get("title"), category);
    }

}
