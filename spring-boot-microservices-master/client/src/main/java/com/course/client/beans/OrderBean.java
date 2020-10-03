package com.course.client.beans;

import java.util.List;

public class OrderBean {
    private Long id;

    private List<CartItemBean> products;

    private double total;

    public void setTotal(double total) {
        this.total = total;
    }

    public double getTotal() {
        return total;
    }

    public OrderBean() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<CartItemBean> getProducts() {
        return this.products;
    }

    public void setProducts(List<CartItemBean> products) {
        this.products = products;
    }

    public void calculateTotal() {
        total = 0;
        for (CartItemBean item: products) {
            total += item.getQuantity() * item.getProduct().getPrice();
        }
    }
}
