package com.epam.esm.entities;

import org.springframework.hateoas.RepresentationModel;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Objects;

@Entity(name = "Order")
@Table(name = "orders")
public class Order extends RepresentationModel<Tag> implements EntityInterface {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "cost", precision = 9, scale = 2)
    private BigDecimal cost;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "certificate_id", referencedColumnName = "id")
    private Certificate certificate;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Column(name = "date")
    private ZonedDateTime date;

    public Order() {
    }

    public Order(Long id, BigDecimal cost, Certificate certificate, User user, ZonedDateTime orderDate) {
        this.id = id;
        this.cost = cost;
        this.certificate = certificate;
        this.user = user;
        this.date = orderDate;
    }

    public Order(Long id, BigDecimal cost, Certificate certificate, User user, String orderDate) {
        this.id = id;
        this.cost = cost;
        this.certificate = certificate;
        this.user = user;
        this.date = ZonedDateTime
                .parse(orderDate);
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public Long getId() {
        return id;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public Certificate getCertificate() {
        return certificate;
    }

    public void setCertificate(Certificate certificate) {
        this.certificate = certificate;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", cost=" + cost +
                ", certificate=" + certificate +
                ", user=" + user +
                ", date=" + date +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Order order = (Order) o;
        return Objects.equals(id, order.id) &&
                (cost.compareTo(order.cost) == 0) &&
                Objects.equals(certificate, order.certificate) &&
                Objects.equals(user, order.user) &&
                Objects.equals(date, order.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, cost, certificate, date);
    }
}


