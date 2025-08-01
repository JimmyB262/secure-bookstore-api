package org.example.entity;

import jakarta.json.bind.annotation.JsonbTransient;
import jakarta.persistence.*;

@Entity
public class Stock {

    @Id
    @OneToOne
    @JoinColumn(name = "id")
    @JsonbTransient
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


    @JsonbTransient
    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
