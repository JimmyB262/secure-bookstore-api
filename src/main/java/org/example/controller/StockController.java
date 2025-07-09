package org.example.controller;


import jakarta.annotation.security.DeclareRoles;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.Stateless;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.example.dto.BookAuthorDTO;
import org.example.dto.BookStockDTO;
import org.example.entity.Book;
import org.example.entity.Stock;
import org.example.repository.BookRepository;
import org.example.repository.StockRepository;

import java.util.Optional;
import java.util.List;

@Stateless
@DeclareRoles({"user", "admin"})
@Path("/stock")
@RequestScoped
public class StockController {

    @Inject
    StockRepository  stockRepo;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"admin" , "user"})
    public List<BookStockDTO> stock(){

        return stockRepo.getStock();

    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    @RolesAllowed({"admin"})
    public Response setStockWithIsbn(BookStockDTO bookStockDTO){

        Integer id = bookStockDTO.getId();
        BookStockDTO bookStockDTO1 = stockRepo.getStockById(id);
        if (bookStockDTO1 == null){
            bookStockDTO1 = stockRepo.saveStock(bookStockDTO);
            return Response.ok(bookStockDTO1).build();
        }else{
            return Response.status(Response.Status.NOT_ACCEPTABLE).build();
        }


    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    @RolesAllowed({"admin"})
    public Response updateStock(BookStockDTO bookStockDTO){

        BookStockDTO stock1 = stockRepo.updateStock(bookStockDTO);

        if (stock1 == null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok(stock1).build();

    }


    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    @RolesAllowed({"admin"})
    public Response deleteStock(@PathParam("id") Integer id){
        Optional<BookStockDTO> opt = stockRepo.deleteStock(id);

        if (!opt.isPresent()){
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok(opt.get()).build();

    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"user" , "admin"})
    public Response seeStockWithBookId(@PathParam("id") Integer id){

        BookStockDTO bookStockDTO = stockRepo.getStockById(id);

        if (bookStockDTO == null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok(bookStockDTO).build();

    }

    @GET
    @Path("/max")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"user" , "admin"})
    public List<BookStockDTO> seeBookWithMaxStock(){

        return stockRepo.getMaxStock();

    }

    @GET
    @Path("/min")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"user" , "admin"})
    public List<BookStockDTO> seeBookWithMinStock(){

        return stockRepo.getMinStock();

    }


    @GET
    @Path("/avg")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"user" , "admin"})
    public Double seeAvgStockAllBooks(){

        return stockRepo.getAverageQuantity();

    }

    @GET
    @Path("/sortedlist")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"user" , "admin"})
    public List<BookStockDTO> seeSortedByQuantityList(){

        return stockRepo.getSortedList();

    }

    @GET
    @Path("/authorsum/{id}")
    @RolesAllowed({"user" , "admin"})
    @Produces(MediaType.APPLICATION_JSON)
    public Integer seeAuthorSumOfBooks(@PathParam("id") Integer id){

        return stockRepo.getSumOfAuthorBooks(id);

    }
}
