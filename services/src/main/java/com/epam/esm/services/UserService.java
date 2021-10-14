package com.epam.esm.services;

import com.epam.esm.entities.User;
import com.epam.esm.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements CrudService<User> {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> getUserWithMaxOrders() {
        return userRepository.findUsersBySumOfOrders();
    }

    @Override
    public List<User> getAll(Integer currentPage, Integer itemsPerPage) {
        Integer startPosition = currentPage * itemsPerPage - itemsPerPage;
        List<User> users = userRepository.findAll(startPosition, itemsPerPage);
        return users;
    }

    @Override
    public Optional<User> get(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public void save(User user) {
        //unsupported operation for User
    }

    @Override
    public void update(User user) {
        //unsupported operation for User

    }

    @Override
    public void delete(Long id) {
        //unsupported operation for User
    }
}
