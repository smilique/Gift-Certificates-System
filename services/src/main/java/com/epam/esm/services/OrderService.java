package com.epam.esm.services;

import com.epam.esm.entities.Order;
import com.epam.esm.repositories.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService implements CrudService<Order> {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public List<Order> getAll(Integer currentPage, Integer itemsPerPage) {
        Integer startPosition = currentPage * itemsPerPage - itemsPerPage;
        List<Order> orders = orderRepository.findAll(startPosition, itemsPerPage);
        return orders;
    }

    public List<Order> getByUserId(Long userId, Integer currentPage, Integer itemsPerPage) {
        Integer startPosition = currentPage * itemsPerPage - itemsPerPage;
        return orderRepository.findByUserId(userId, startPosition, itemsPerPage);
    }

    @Override
    public void save(Order order) {
        orderRepository.save(order);
    }

    @Override
    public Optional<Order> get(Long id) {
        return orderRepository.findById(id);
    }

    @Override
    public void update(Order order) {

        //unsupported operation for Order
    }

    @Override
    public void delete(Long id) {

        //unsupported operation for Order
    }
}
