package org.example;

import jakarta.json.bind.annotation.JsonbPropertyOrder;
import jakarta.persistence.*;

import java.math.BigDecimal;


@Entity
@JsonbPropertyOrder({ "id", "title", "author", "stock" })
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer  id;

    private String title;
    private String isbn;
    private BigDecimal price;
    private Integer author_id;

    public Book(Integer id, String title, String isbn, BigDecimal price, Integer author_id) {
        this.id = id;
        this.title = title;
        this.isbn = isbn;
        this.price = price;
        this.author_id = author_id;
    }


    public Book() {

    }

    public Integer getAuthor_id() {
        return author_id;
    }

    public void setAuthor_id(Integer author_id) {
        this.author_id = author_id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
