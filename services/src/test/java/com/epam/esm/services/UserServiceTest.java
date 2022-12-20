package com.epam.esm.services;

import com.epam.esm.entities.User;
import com.epam.esm.repositories.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    private static final User FIRST_USER = new User(1L, "First user", BigDecimal.TEN);
    private static final User SECOND_USER = new User(2L, "Second User", BigDecimal.ONE);
    private static final List<User> USERS = Arrays.asList(FIRST_USER, SECOND_USER);

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserService userService;

    @Test
    void testUserServiceShouldReturnListOfUsers() {
        //given
        Mockito.when(userRepository.findAll(0,2))
                .thenReturn(USERS);
        //when
        List<User> actual = userService.getAll(1,2);
        //then
        Assertions.assertEquals(USERS, actual);
    }

    @Test
    void testUserServiceShouldReturnUserWhenIdSubmitted() {
        //given
        Long userId = FIRST_USER.getId();
        Optional<User> expected = Optional.of(FIRST_USER);
        Mockito.when(userRepository.findById(userId))
                .thenReturn(expected);
        //when
        Optional<User> actual = userService.get(userId);
        //then
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testUserServiceShouldFindUserWithMaxSumOfOrders() {
        //given
        Optional<User> expected = Optional.of(SECOND_USER);
        Mockito.when(userRepository.findUsersBySumOfOrders())
                .thenReturn(expected);
        //when
        Optional<User> actual = userService.getUserWithMaxOrders();

        //then
        Assertions.assertEquals(expected, actual);
    }


}