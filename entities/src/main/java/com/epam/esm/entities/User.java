package com.epam.esm.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name="user")
public class User extends RepresentationModel<Tag> implements EntityInterface {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private BigDecimal balance;

    @OneToMany(fetch = FetchType.EAGER, targetEntity = User.class)
    @JoinTable(name = "orders", joinColumns =
            {@JoinColumn(name = "user_id", referencedColumnName = "id")})
    @JsonProperty("orders")
    private List<Order> orders;

    public User() {
    }

    public User(Long id, String name, BigDecimal balance, List<Order> orders) {
        this.id = id;
        this.name = name;
        this.balance = balance;
        this.orders = orders;
    }

    public User(Long id, String name, BigDecimal balance) {
        this.id = id;
        this.name = name;
        this.balance = balance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public void setOrders(Order... orders) {
        setOrders(Arrays.asList(orders));
    }

    public void addOrder(Order order) {
        orders.add(order);
    }

    public void addOrders(Order... additionalOrders) {
        orders.addAll(
                Arrays.asList(additionalOrders));
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", balance=" + balance +
                ", orders=" + orders +
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
        User user = (User) o;
        return Objects.equals(id, user.id) &&
                Objects.equals(name, user.name) &&
                (balance.compareTo(
                        user.balance) == 0) &&
                Objects.equals(orders, user.orders);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, balance, orders);
    }
}
