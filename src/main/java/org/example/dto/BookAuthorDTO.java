package org.example.dto;

import jakarta.json.bind.annotation.JsonbPropertyOrder;

import java.math.BigDecimal;

@JsonbPropertyOrder({ "id", "title", "isbn", "price", "author_id" })
public class BookAuthorDTO {

    private Integer id;
    private String title;
    private String isbn;
    private BigDecimal price;
    private Integer author_id;

    public BookAuthorDTO(Integer id, String title, String isbn, BigDecimal price, Integer author_id) {
        this.author_id = author_id;
        this.id = id;
        this.isbn = isbn;
        this.price = price;
        this.title = title;
    }

    public BookAuthorDTO() {
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
