package org.example;

import jakarta.json.bind.annotation.JsonbPropertyOrder;
import jakarta.persistence.*;


@Entity
@JsonbPropertyOrder({ "id", "title", "author", "stock" })
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer  id;

    private String title;
    private String author;
    @Column(name = "is_in_stock")
    private boolean stock;

    public Book(Integer id, String author, boolean stock, String title) {
        this.id = id;
        this.author = author;
        this.stock = stock;
        this.title = title;
    }

    public Book() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public boolean isStock() {
        return stock;
    }

    public void setStock(boolean stock) {
        this.stock = stock;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
