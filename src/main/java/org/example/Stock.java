package org.example;

import jakarta.persistence.*;

@Entity
public class Stock {

    private Integer  id;
    private Integer quantity;

    public Stock(Integer id, Integer quantity) {
        this.id = id;
        this.quantity = quantity;
    }

    public Stock() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
