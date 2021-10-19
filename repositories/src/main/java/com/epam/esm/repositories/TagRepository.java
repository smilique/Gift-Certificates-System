package com.epam.esm.repositories;

import com.epam.esm.entities.Tag;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.Optional;

@Repository
public class TagRepository implements CrudRepositoryInterface<Tag> {

    private static final String SELECT_ALL = "from Tag order by id";
    private static final String SELECT_BY_ID = "from Tag where id = :id";
    private static final String SELECT_BY_NAME = "from Tag where name = :name";
    private static final String SELECT_BY_POPULARITY = "select tag.id, tag.name from tag join tagged_gift_certificate tgc on tag.id = tgc.tag_id where gift_certificate_id in (" +
            "select certificate_id from orders where user_id = (select user_id from orders group by user_id order by sum(orders.cost) desc limit 1)) " +
            "group by tag_id order by count(tag_id) desc limit 1";
    private static final String DELETE_BY_ID = "delete from Tag where id = :id";

    @PersistenceContext
    private Session session;

    public TagRepository() {
    }

    @Override
    public List<Tag> findAll(Integer startPosition, Integer itemsPerPage) {
        Query query = session.createQuery(SELECT_ALL);
        query.setFirstResult(startPosition);
        query.setMaxResults(itemsPerPage);
        List<Tag> tags = query.getResultList();
        return tags;
    }

    public Optional<Tag> findByName(String name) {
        Query query = session.createQuery(SELECT_BY_NAME);
        query.setParameter("name", name);
        Tag tag = (Tag) query.getSingleResult();
        return Optional.of(tag);
    }

    @Override
    public Optional<Tag> findById(Long id) {
        Query query = session.createQuery(SELECT_BY_ID);
        query.setParameter("id", id);
        Tag tag = (Tag) query.getSingleResult();
        return Optional.of(tag);
    }

    @Override
    @Transactional
    public void save(Tag tag) {
        saveNew(tag);
    }

    @Transactional
    public Long saveNew(Tag tag) {
        session.persist(tag);
        session.flush();
        return tag.getId();
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Query query = session.createQuery(DELETE_BY_ID);
        query.setParameter("id", id);
        query.executeUpdate();
    }

    @Override
    public void update(Tag entity) {

        //unsupported operation for Tag
    }

    public Optional<Tag> findMostUsedTag() {
        Query query = session.createNativeQuery(SELECT_BY_POPULARITY, Tag.class);
        Tag tag = (Tag) query.getSingleResult();
        return Optional.of(tag);
    }
}