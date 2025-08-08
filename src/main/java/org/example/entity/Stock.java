package org.example.entity;

import jakarta.json.bind.annotation.JsonbTransient;
import jakarta.persistence.*;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
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
        if (book != null && book.getStock() != this) {
            book.setStock(this);
        }
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
