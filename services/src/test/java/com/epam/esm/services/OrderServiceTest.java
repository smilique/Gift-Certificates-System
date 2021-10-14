package com.epam.esm.services;

import com.epam.esm.entities.Certificate;
import com.epam.esm.entities.Order;
import com.epam.esm.entities.User;
import com.epam.esm.repositories.OrderRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    private static final User FIRST_USER = new User(1L, "First user", BigDecimal.TEN);
    private static final User SECOND_USER = new User(2L, "Second User", BigDecimal.ONE);
    private static final List<User> USERS = Arrays.asList(FIRST_USER, SECOND_USER);
    private static final Certificate FIRST_CERTIFICATE = new Certificate(1L, "B# Test certificate 1",
            "Certificate 3 description",
            BigDecimal.valueOf(1300), 30L,
            ZonedDateTime.parse("2018-08-29T06:12:15.100Z"),
            ZonedDateTime.parse("2018-08-29T06:12:15.100Z"));
    private static final Certificate SECOND_CERTIFICATE = new Certificate(2L, "A# Test certificate 2",
            "Certificate 3 description",
            BigDecimal.valueOf(2000), 90L,
            ZonedDateTime.parse("2018-10-29T06:12:15.200Z"),
            ZonedDateTime.parse("2018-10-29T06:12:15.200Z"));
    private static final Order FIRST_ORDER = new Order(1L, BigDecimal.valueOf(1300), FIRST_CERTIFICATE, FIRST_USER, ZonedDateTime.now());
    private static final Order SECOND_ORDER = new Order(2L, BigDecimal.valueOf(2000), SECOND_CERTIFICATE, SECOND_USER, ZonedDateTime.now());
    private static final List<Order> ORDERS = Arrays.asList(FIRST_ORDER, SECOND_ORDER);
    private static final int WANTED_NUMBER_OF_INVOCATIONS = 1;

    @Mock
    OrderRepository orderRepository;

    @InjectMocks
    OrderService orderService;

    @Test
    void testOrderServiceShouldReturnListOfOrders() {
        //given
        Mockito.when(orderRepository.findAll(0, 2))
                .thenReturn(ORDERS);
        //when
        List<Order> actual = orderService.getAll(1, 2);
        //then
        Assertions.assertEquals(ORDERS, actual);
    }

    @Test
    void testOrderServiceShouldReturnOrderById() {
        //given
        Long orderId = FIRST_ORDER.getId();
        Optional<Order> expected = Optional.of(FIRST_ORDER);
        Mockito.when(orderRepository.findById(orderId))
                .thenReturn(expected);
        //when
        Optional<Order> actual = orderService.get(orderId);
        //then
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testOrderServiceShouldReturnListOfOrdersByUserId() {
        //given
        User user = FIRST_ORDER.getUser();
        Long userId = user.getId();
        List<Order> expected = Collections.singletonList(FIRST_ORDER);
        Mockito.when(orderRepository.findByUserId(userId, 0, 2))
                .thenReturn(expected);
        //when
        List<Order> actual = orderService.getByUserId(userId, 1, 2);
        //then
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testOrderServiceShouldPassOrderToRepositoryWhenSaving() {
        //given
        //when
        orderService.save(FIRST_ORDER);
        //then
        Mockito.verify(orderRepository,
                Mockito.times(WANTED_NUMBER_OF_INVOCATIONS)).save(FIRST_ORDER);
    }

    @Test
    void testOrderServiceShouldPassOrderToRepositoryWhenUpdating() {
        //given
        //when
        orderService.update(FIRST_ORDER);
        //then
        Mockito.verify(orderRepository,
                Mockito.times(WANTED_NUMBER_OF_INVOCATIONS)).save(FIRST_ORDER);
    }


}