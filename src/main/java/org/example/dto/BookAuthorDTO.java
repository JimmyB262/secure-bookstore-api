package org.example.dto;

import jakarta.json.bind.annotation.JsonbPropertyOrder;

import java.math.BigDecimal;
import java.util.Base64;

@JsonbPropertyOrder({ "id", "title", "isbn", "price", "author_id" })
public class BookAuthorDTO {

    private Integer id;
    private String title;
    private String isbn;
    private BigDecimal price;
    private Integer author_id;
    private String coverImage;
    private String imageContentType;

    public BookAuthorDTO(Integer id, String title, String isbn, BigDecimal price, Integer author_id, byte[] coverImage, String imageContentType) {
        this.id = id;
        this.title = title;
        this.isbn = isbn;
        this.price = price;
        this.author_id = author_id;
        this.coverImage = Base64.getEncoder().encodeToString(coverImage);
        this.imageContentType = imageContentType;
    }

    public BookAuthorDTO(Integer id, String title, String isbn, BigDecimal price, Integer author_id) {
        this.id = id;
        this.title = title;
        this.isbn = isbn;
        this.price = price;
        this.author_id = author_id;
        this.coverImage = null;
        this.imageContentType = null;
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

    public String getImageContentType() {
        return imageContentType;
    }

    public void setImageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }
}
