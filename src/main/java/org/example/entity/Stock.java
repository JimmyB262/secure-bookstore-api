package org.example.entity;

import jakarta.json.bind.annotation.JsonbTransient;
import jakarta.persistence.*;

@Entity
public class Stock {

    @Id
    @OneToOne
    @JoinColumn(name = "id")

    private Book book;

    private Integer quantity;

    public Stock(Book book, Integer quantity) {
        this.book = book;
        this.quantity = quantity;
    }

    public Stock() {
    }

    public Integer getBookId() {
        return book != null ? book.getId() : null;
    }



    public Book getId() {
        return book;
    }

    public void setId(Book book) {
        this.book = book;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
