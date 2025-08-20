package org.example.dto;


import jakarta.json.bind.annotation.JsonbPropertyOrder;

@JsonbPropertyOrder({"id", "quantity" })
public class BookStockDTO {


    private Integer id;
    private Integer quantity;

    public BookStockDTO(Integer id ,Integer quantity ) {
        this.id = id;
        this.quantity = quantity;
    }

    public BookStockDTO() {
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

    @Override
    public String toString() {
        return "Stock{id=" + id + ", quantity='" + quantity + "}";
    }
}
