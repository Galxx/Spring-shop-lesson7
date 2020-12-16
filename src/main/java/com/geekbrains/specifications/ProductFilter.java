package com.geekbrains.specifications;

import com.geekbrains.entities.Product;
import com.geekbrains.repositories.ProductSpecifications;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

@Getter
public class ProductFilter {
    private Specification<Product> spec;
    private StringBuilder filterDefinition;

    public ProductFilter(Map<String, String> map, List<String> categories) {
        this.spec = Specification.where(null);
        //this.filterDefinition = new StringBuilder();

        String minPrice = map.get("min_price");
        String maxPrice = map.get("max_price");
        String title = map.get("title");

        if(StringUtils.isNotEmpty(minPrice)) {
            int minPriceInteger = Integer.parseInt(minPrice);
            spec = spec.and(ProductSpecifications.priceGreaterOrEqualsThen(minPriceInteger));
            //filterDefinition.append("&min_price=").append(minPrice)
        }

        if(StringUtils.isNotEmpty(maxPrice)) {
            int maxPriceInteger = Integer.parseInt(maxPrice);
            spec = spec.and(ProductSpecifications.priceLesserOrEqualsThen(maxPriceInteger));
        }

        if(StringUtils.isNotEmpty(title)) {
            spec = spec.and(ProductSpecifications.titleLike(title));
        }

        if(!CollectionUtils.isEmpty(categories)) {
            Specification specCategories = null;
            for(String c: categories) {
                if(specCategories == null) {
                    specCategories = ProductSpecifications.categoryIs(c);
                } else {
                    specCategories = specCategories.or(ProductSpecifications.categoryIs(c));
                }
            }

            spec = spec.and(specCategories);
        }
    }

}
