package com.epam.esm.repositories;

import com.epam.esm.entities.Role;
import com.epam.esm.entities.User;
import org.hibernate.Session;

import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.Optional;

public class RoleRepository implements CrudRepositoryInterface<Role> {

    private static final String GET_ALL = "from Role order by id";
    private static final String GET_BY_ID = "from Role where id = :id";

    @PersistenceContext
    private Session session;

    @Override
    public List<Role> findAll(Integer startPosition, Integer itemsPerPage) {

        Query query = session.createQuery(GET_ALL);
        query.setFirstResult(startPosition);
        query.setMaxResults(itemsPerPage);
        return query.getResultList();
    }

    @Override
    public Optional<Role> findById(Long id) {
        Query query = session.createQuery(GET_BY_ID);
        query.setParameter("id", id);
        Role role = (Role) query.getSingleResult();
        return Optional.of(role);
    }

    @Override
    public void save(Role entity) {
        //not supported
    }

    @Override
    public void delete(Long id) {
        //not supported
    }

    @Override
    public void update(Role entity) {
        //not supported
    }
}
