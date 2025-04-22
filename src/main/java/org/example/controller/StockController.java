package org.example.controller;


import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.example.dto.BookStockDTO;
import org.example.entity.Book;
import org.example.entity.Stock;
import org.example.repository.BookRepository;
import org.example.repository.StockRepository;

import java.util.Optional;
import java.util.List;
@Path("/stock")
@RequestScoped
public class StockController {

    @Inject
    StockRepository  stockRepo;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Stock> stock(){

        return stockRepo.getStock();

    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response seeStockWithBookId(@PathParam("id") Integer id){

        Stock stock = stockRepo.getStockById(id);

        if (stock == null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok(stock).build();

    }

    @GET
    @Path("/max")
    @Produces(MediaType.APPLICATION_JSON)
    public BookStockDTO seeBookWithMaxStock(){

        return stockRepo.getMaxStock();

    }

    @GET
    @Path("/min")
    @Produces(MediaType.APPLICATION_JSON)
    public BookStockDTO seeBookWithMinStock(){

        return stockRepo.getMinStock();

    }


    @GET
    @Path("/avg")
    @Produces(MediaType.APPLICATION_JSON)
    public Double seeAvgStockAllBooks(){

        return stockRepo.getAverageQuantity();

    }

    @GET
    @Path("/sortedlist")
    @Produces(MediaType.APPLICATION_JSON)
    public List<BookStockDTO> seeSortedByQuantityList(){

        return stockRepo.getSortedList();

    }

    @GET
    @Path("/authorsum/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Integer seeAuthorSumOfBooks(@PathParam("id") Integer id){

        return stockRepo.getSumOfAuthorBooks(id);
    }
}
