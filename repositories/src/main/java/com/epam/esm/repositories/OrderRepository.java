package com.epam.esm.repositories;

import com.epam.esm.entities.Order;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.Optional;

@Repository
public class OrderRepository implements CrudRepositoryInterface<Order> {

    private static final String GET_ALL = "from Order";
    private static final String GET_BY_ID = "from Order where id = :id";
    private static final String GET_BY_USER_ID = "select o from Order o join o.user u where u.id = :userId";

    @PersistenceContext
    private Session session;

    public OrderRepository() {
    }

    @Override
    public List<Order> findAll(Integer startPosition, Integer itemsPerPage) {
        Query query = session.createQuery(GET_ALL);
        query.setFirstResult(startPosition);
        query.setMaxResults(itemsPerPage);
        List<Order> orders = query.getResultList();
        return orders;
    }

    @Override
    public Optional<Order> findById(Long id) {
        Query query = session.createQuery(GET_BY_ID);
        query.setParameter("id", id);
        Order order = (Order) query.getSingleResult();
        return Optional.of(order);
    }

    public List<Order> findByUserId(Long userId, Integer startPosition, Integer itemsPerPage) {
        Query query = session.createQuery(GET_BY_USER_ID);
        query.setParameter("userId", userId);
        query.setFirstResult(startPosition);
        query.setMaxResults(itemsPerPage);
        List<Order> orders = query.getResultList();
        return orders;
    }

    @Override
    @Transactional
    public void save(Order order) {
        session.persist(order);
        session.flush();
    }

    @Override
    public void delete(Long id) {

        //unsupported operation for Order
    }

    @Override
    public void update(Order entity) {

        //unsupported operation for Order
    }
}
