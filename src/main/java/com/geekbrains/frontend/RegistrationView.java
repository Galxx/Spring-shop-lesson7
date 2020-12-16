package com.geekbrains.frontend;

import com.geekbrains.entities.User;
import com.geekbrains.repositories.UserRepository;
import com.geekbrains.services.UserService;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

@Route("registration")
@PageTitle("Registration | Vaadin Show")
public class RegistrationView extends AbstractView {
    private final UserService userService;

    public RegistrationView(UserService userService) {
        this.userService = userService;
        initRegistrationView();
    }

    private void initRegistrationView() {
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        TextField phoneTextField = initTextFieldWithPlaceholder("Номер телефона");
        TextField passwordTextField = initTextFieldWithPlaceholder("Пароль");
        TextField firstNameTextField = initTextFieldWithPlaceholder("Имя");
        TextField lastNameTextField = initTextFieldWithPlaceholder("Фамилия");
        TextField emailTextField = initTextFieldWithPlaceholder("Email");
        TextField ageTextField = initTextFieldWithPlaceholder("Возраст");
        Button registrationButton = new Button("Зарегистрироваться", event -> {
            boolean hasError = false;

            if(!phoneTextField.getValue().matches("\\d+")) {
                Notification.show("Телефон должен состоять из цифр");
                hasError = true;
            }

            if(!firstNameTextField.getValue().matches("[а-яА-Я]+")) {
                Notification.show("Имя должно состоять из букв");
                hasError = true;
            }

            if(!lastNameTextField.getValue().matches("[а-яА-Я]+")) {
                Notification.show("фамилия должно состоять из букв");
                hasError = true;
            }

            if(!ageTextField.getValue().matches("\\d+") || Integer.parseInt(ageTextField.getValue()) < 18) {
                Notification.show("Возраст должен состоять из цифр и вы должны быть старше 18 лет");
                hasError = true;
            }

            if(hasError) {
                return;
            } else {
                User user = userService.saveUser(phoneTextField.getValue(), passwordTextField.getValue(), firstNameTextField.getValue(),
                        lastNameTextField.getValue(), emailTextField.getValue(), ageTextField.getValue());

                UI.getCurrent().navigate("login");
            }
        });

        add(phoneTextField, passwordTextField, firstNameTextField, lastNameTextField, emailTextField, ageTextField, registrationButton);
    }
}
