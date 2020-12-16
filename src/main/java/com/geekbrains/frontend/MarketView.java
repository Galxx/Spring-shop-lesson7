package com.geekbrains.frontend;

import com.geekbrains.entities.Product;
import com.geekbrains.repositories.ProductRepository;
import com.geekbrains.security.CustomPrincipal;
import com.geekbrains.services.CartService;
import com.geekbrains.specifications.ProductFilter;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Route("market")
public class MarketView extends AbstractView {

    private final CartService cartService;
    private final ProductRepository productRepository;
    private final Authentication authentication;

    private TextField titleTextField;
    private TextField minPriceTextField;
    private TextField maxPriceTextField;
    private Grid<Product> productGrid;

    public MarketView(CartService cartService,
                      ProductRepository productRepository) {
        this.cartService = cartService;
        this.productRepository = productRepository;
        this.authentication = SecurityContextHolder.getContext().getAuthentication();
        initMarketPage();
    }

    private void initMarketPage() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(new Button("Главная", e -> System.out.println("Главная")));
        horizontalLayout.add(new Button("Корзина", e -> UI.getCurrent().navigate("cart")));
        horizontalLayout.add(new Button("Мои заказы", e -> UI.getCurrent().navigate("orders")));

        Button otherOrdersButton = new Button("Заказы пользователей", e-> UI.getCurrent().navigate("other-orders"));
        if(isManagerOrAdmin()) {
            horizontalLayout.add(otherOrdersButton);
        }

        productGrid = new Grid<>(Product.class);
        productGrid.setWidth("60%");
        productGrid.setColumns("id", "title", "price");
        productGrid.setSelectionMode(Grid.SelectionMode.MULTI);
        List<Product> productList = productRepository.findAll();
        productGrid.setItems(productList);

        setDefaultHorizontalComponentAlignment(Alignment.CENTER);

        setHorizontalComponentAlignment(Alignment.CENTER, productGrid);
        add(horizontalLayout);
        add(initFilterComponent());
        add(productGrid);

        add(new Button("Добавить выбранные товары", e -> {
            Set<Product> productSet = productGrid.getSelectedItems();
            productSet.stream().forEach(cartService::add);
            Notification.show("Товар успешно добавлен в корзину");
        }));
    }

    private boolean isManagerOrAdmin() {
        return authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_MANAGER")
                || a.getAuthority().equals("ROLE_ADMIN"));
    }

    private Component initFilterComponent() {
        HorizontalLayout priceLayout = new HorizontalLayout();
        titleTextField = initTextFieldWithPlaceholder("Название");
        minPriceTextField = initTextFieldWithPlaceholder("Минимальная цена");
        maxPriceTextField = initTextFieldWithPlaceholder("Максимальная цена");
        priceLayout.add(titleTextField, minPriceTextField, maxPriceTextField);
        priceLayout.setAlignItems(Alignment.CENTER);

        VerticalLayout categoriesLayout = new VerticalLayout();
        Label categoriesLabel = new Label("Категории");
        Checkbox foodCheckbox = new Checkbox("Food");
        Checkbox devicesCheckbox = new Checkbox("Devices");
        categoriesLayout.add(categoriesLabel, foodCheckbox, devicesCheckbox);
        categoriesLayout.setAlignItems(Alignment.CENTER);

        HorizontalLayout buttonLayout = new HorizontalLayout();
        Button makeFiltersButton = new Button("Применить", event -> {
            Map<String, String> filterMap = new HashMap<>();
            List<String> categories = new ArrayList<>();
            if (StringUtils.isNotEmpty(titleTextField.getValue())) {
                filterMap.put("title", titleTextField.getValue());
            }

            if (StringUtils.isNotEmpty(minPriceTextField.getValue())) {
                filterMap.put("min_price", minPriceTextField.getValue());
            }

            if (StringUtils.isNotEmpty(maxPriceTextField.getValue())) {
                filterMap.put("max_price", maxPriceTextField.getValue());
            }

            if (foodCheckbox.getValue()) {
                categories.add("Food");
            }

            if (devicesCheckbox.getValue()) {
                categories.add("Devices");
            }

            productGrid.setItems(
                    productRepository.findAll(
                            new ProductFilter(filterMap, categories).getSpec(), PageRequest.of(0, 10)).getContent()
            );
        });

        Button cancelFiltersButton = new Button("Сброс фильтра", event -> productGrid.setItems(productRepository.findAll()));
        buttonLayout.add(makeFiltersButton, cancelFiltersButton);

        VerticalLayout filterComponentsLayout = new VerticalLayout(priceLayout, categoriesLayout, buttonLayout);
        filterComponentsLayout.setAlignItems(Alignment.CENTER);
        return filterComponentsLayout;
    }

}
