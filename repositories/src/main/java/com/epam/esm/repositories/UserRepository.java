package com.epam.esm.repositories;

import com.epam.esm.entities.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.Optional;

@Repository
public class UserRepository implements CrudRepositoryInterface<User> {

    private static final String GET_ALL = "from User order by id";
    private static final String GET_BY_ID = "from User where id = :id";
    private static final String GET_BY_SUM_OF_ORDERS = "select user.id, user.name, user.balance from user " +
            "join orders on user.id = orders.user_id " +
            "where user.id = (select user_id from orders group by user_id order by sum(orders.cost) desc limit 1) group by user.id";

    @PersistenceContext()
    private EntityManager entityManager;

    public UserRepository() {
    }

    @Override
    public List<User> findAll(Integer startPosition, Integer itemsPerPage) {
        Query query = entityManager.createQuery(GET_ALL);
        query.setFirstResult(startPosition);
        query.setMaxResults(itemsPerPage);
        List<User> users = query.getResultList();
        return users;
    }

    @Override
    public Optional<User> findById(Long id) {
        Query query = entityManager.createQuery(GET_BY_ID);
        query.setParameter("id", id);
        User user = (User) query.getSingleResult();
        return Optional.of(user);
    }

    public Optional<User> findUsersBySumOfOrders() {
        Query query = entityManager.createNativeQuery(GET_BY_SUM_OF_ORDERS, User.class);
        User user = (User) query.getSingleResult();
        return Optional.of(user);
    }

    @Override
    public void save(User entity) {

        //Not supported operation for User
    }

    @Override
    public void delete(Long id) {

        //Not supported operation for User
    }

    @Override
    public void update(User entity) {

        //Not supported operation for User

    }
}
