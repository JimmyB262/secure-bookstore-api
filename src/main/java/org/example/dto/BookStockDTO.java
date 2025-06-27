package org.example.dto;


import jakarta.json.bind.annotation.JsonbPropertyOrder;

@JsonbPropertyOrder({"quantity" , "id" })
public class BookStockDTO {

    private Integer quantity;
    private Integer id;


    public BookStockDTO(Integer quantity , Integer id) {
        this.quantity = quantity;
        this.id = id;
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
}
