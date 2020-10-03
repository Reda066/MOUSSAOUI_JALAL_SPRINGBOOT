package com.course.order.domain;

import javax.persistence.*;
import java.util.List;
import com.course.order.domain.OrderItem;

@Entity
public class OrderT {

    @Id
    @GeneratedValue
    private Long id;

    private Double total;

    @OneToMany(cascade = CascadeType.ALL)
    private List<OrderItem> products;

    public OrderT() {
    }

    public OrderT(Long id, Double total, List<OrderItem> products) {
        this.id = id;
        this.total = total;
        this.products = products;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public void setProducts(List<OrderItem> products) {
        this.products = products;
    }

    public Long getId() {
        return id;
    }

    public Double getTotal() {
        return total;
    }

    public List<OrderItem> getProducts() {
        return products;
    }


}
