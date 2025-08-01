package org.example.entity;

import jakarta.json.bind.annotation.JsonbPropertyOrder;
import jakarta.json.bind.annotation.JsonbTransient;
import jakarta.persistence.*;

import java.math.BigDecimal;


@Entity
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer  id;

    private String title;
    private String isbn;
    private BigDecimal price;

    @ManyToOne
    @JoinColumn(name = "author_id")
    @JsonbTransient
    private Author author; //TODO thelw na deixnei to id sto json

    public Book(Integer id, String title, String isbn, BigDecimal price, Author author) {
        this.id = id;
        this.title = title;
        this.isbn = isbn;
        this.price = price;
        this.author = author;
    }

    public Integer getAuthorId() {
        return author != null ? author.getAuthor_id() : null;
    }


    public Book() {

    }

    @JsonbTransient
    public Author getAuthor_id() {
        return author;
    }

    public void setAuthor_id(Author author) {
        this.author = author;
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
