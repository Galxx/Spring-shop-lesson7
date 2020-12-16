package com.geekbrains;

import com.geekbrains.Application;
import com.geekbrains.controllers.dto.RoleDto;
import com.geekbrains.controllers.dto.UserDto;
import com.geekbrains.entities.Role;
import com.geekbrains.entities.User;
import com.geekbrains.exceptions.ManagerIsEarlierThanNeedException;
import com.geekbrains.exceptions.RoleNotFoundException;
import com.geekbrains.exceptions.UserNotFoundException;
import com.geekbrains.repositories.RoleRepository;
import com.geekbrains.repositories.UserRepository;
import com.geekbrains.services.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@SpringBootTest(classes = Application.class)
public class UserServiceTest {
    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private RoleRepository roleRepository;

    @Test
    public void findOneUserTest() {
        User userFromDB = new User();
        userFromDB.setId(1L);
        userFromDB.setPhone("123");
        userFromDB.setEmail("123@mail.ru");

        Mockito.doReturn(Optional.of(userFromDB))
                .when(userRepository)
                .findById(1L);


        User userVitalii = userService.findById(1);
        Assertions.assertNotNull(userVitalii);
        Mockito.verify(userRepository, Mockito.times(1)).findById(ArgumentMatchers.eq(1L));
//        Mockito.verify(userRepository, Mockito.times(1)).findOneByPhone(ArgumentMatchers.any(String.class));
    }

    @Test
    public void checkThrow() {
        User userFromDB = new User();
        userFromDB.setId(1L);
        userFromDB.setPhone("123");
        userFromDB.setEmail("123@mail.ru");

        Mockito.doReturn(Optional.of(userFromDB))
                .when(userRepository)
                .findById(1L);


        Assertions.assertThrows(UserNotFoundException.class, () -> userService.findById(2));
    }

    @Test
    public void saveUserCUSTOMER(){

        UserDto userDto = new UserDto();
        userDto.setPhone("123");
        userDto.setPassword("123");
        userDto.setAge(18);
        userDto.setRoleDto(RoleDto.CUSTOMER);

        User userFromDB = new User();
        userFromDB.setId(1L);
        userFromDB.setPhone("123");
        userFromDB.setEmail("123@mail.ru");

        Mockito.doReturn(userFromDB)
                .when(userRepository)
                .save(Mockito.any(User.class));


        Role role = new Role();
        role.setId(1L);
        role.setName("ROLE_CUSTOMER");

        Mockito.doReturn(Optional.of(role))
                .when(roleRepository)
                .findByName("ROLE_USER");

        User myUser = userService.saveUser(userDto);
        Assertions.assertNotNull(myUser);
    }

    @Test
    public void saveUserMANAGER(){

        UserDto userDto = new UserDto();
        userDto.setPhone("123");
        userDto.setPassword("123");
        //userDto.setAge(17);
        userDto.setAge(19);
        userDto.setRoleDto(RoleDto.MANAGER);

        User userFromDB = new User();
        userFromDB.setId(1L);
        userFromDB.setPhone("123");
        userFromDB.setEmail("123@mail.ru");

        Mockito.doReturn(userFromDB)
                .when(userRepository)
                .save(Mockito.any(User.class));


        Role role = new Role();
        role.setId(2L);
        role.setName("ROLE_MANAGER");

        Mockito.doReturn(Optional.of(role))
                .when(roleRepository)
                .findByName("ROLE_MANAGER");

      //Assertions.assertThrows(ManagerIsEarlierThanNeedException.class, () -> userService.saveUser(userDto));
        User myUser = userService.saveUser(userDto);
        Assertions.assertNotNull(myUser);
    }

    @Test
    public void saveUserExeption(){

        UserDto userDto = new UserDto();
        userDto.setPhone("123");
        userDto.setPassword("123");
        //userDto.setAge(17);
        userDto.setAge(19);
        userDto.setRoleDto(RoleDto.CUSTOMER);

        User userFromDB = new User();
        userFromDB.setId(1L);
        userFromDB.setPhone("123");
        userFromDB.setEmail("123@mail.ru");

        Mockito.doReturn(userFromDB)
                .when(userRepository)
                .save(Mockito.any(User.class));


        Role role = new Role();
        role.setId(2L);
        role.setName("ROLE_MANAGER");

        Mockito.doReturn(Optional.of(role))
                .when(roleRepository)
                .findByName("ROLE_MANAGER");

        Assertions.assertThrows(RoleNotFoundException.class, () -> userService.saveUser(userDto));

    }

    @Test
    public void getAllUsersWithTypeMANAGER() {

        User userFromDB = new User();
        userFromDB.setId(1L);
        userFromDB.setPhone("123");
        userFromDB.setEmail("123@mail.ru");

        Role role = new Role();
        role.setId(2L);
        role.setName("ROLE_MANAGER");

        Mockito.doReturn(Optional.of(role))
                .when(roleRepository)
                .findByName("ROLE_MANAGER");

        Mockito.doReturn(List.of(userFromDB))
                .when(userRepository)
                .findAllByRoles(role);

        List<User> myUsers = userService.getAllUsersWithType(RoleDto.MANAGER);

        Assertions.assertEquals(1, myUsers.size());

    }

    @Test
    public void getAllUsersWithTypeCUSTOMER() {

        User userFromDB = new User();
        userFromDB.setId(1L);
        userFromDB.setPhone("123");
        userFromDB.setEmail("123@mail.ru");

        Role role = new Role();
        role.setId(2L);
        role.setName("ROLE_CUSTOMER");

        Mockito.doReturn(Optional.of(role))
                .when(roleRepository)
                .findByName("ROLE_CUSTOMER");

        Mockito.doReturn(List.of(userFromDB))
                .when(userRepository)
                .findAllByRoles(role);

        List<User> myUsers = userService.getAllUsersWithType(RoleDto.CUSTOMER);

        Assertions.assertEquals(1, myUsers.size());

    }

    @Test
    public void saveUser(){

        User userFromDB = new User();
        userFromDB.setId(1L);
        userFromDB.setPhone("123");
        userFromDB.setEmail("123@mail.ru");

        Mockito.doReturn(userFromDB)
                .when(userRepository)
                .save(Mockito.any());

        User myUser = userService.saveUser("123","123","Sasha","petrov","123@mail.ru","19");
        Assertions.assertNotNull(myUser);
    }

}
