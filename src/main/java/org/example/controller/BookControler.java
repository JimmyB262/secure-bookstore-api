package org.example.controller;


import jakarta.annotation.security.DeclareRoles;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.example.dto.BookAuthorDTO;
import org.example.entity.Book;
import org.example.repository.BookRepository;

import java.util.Base64;
import java.util.Optional;
import java.util.List;

@RequestScoped

@Path("/book")
public class BookControler {


    @EJB
    BookRepository bookRepo; // auto-injected by the container


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"admin" , "user"})
    public List<BookAuthorDTO> books(){

        return bookRepo.getBooks();

    }

    @GET
    @Path("/author/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"user" , "admin"})
    public List<BookAuthorDTO> seeBooksWithAuthorId(@PathParam("id") Integer id){

        return bookRepo.getBooksByAuthorId(id);

    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"user" , "admin"})
    public Response seeBook(@PathParam("id") Integer id){

        BookAuthorDTO bookAuthorDTO = bookRepo.getBookById(id);

        if (bookAuthorDTO != null) {
            byte[] imageBytes = bookRepo.getCoverImageByBookId(id);

            if (imageBytes != null) {
                bookAuthorDTO.setCoverImage(Base64.getEncoder().encodeToString(imageBytes));
            } else {
                bookAuthorDTO.setCoverImage(null);
            }
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok(bookAuthorDTO).build();

    }




    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    @RolesAllowed({"admin"})
    public Response saveBook(BookAuthorDTO bookAuthorDTO){

        BookAuthorDTO bookAuthorDTO1 = bookRepo.saveBook((bookAuthorDTO));
        return Response.ok(bookAuthorDTO).build();

    }


    // POST /book να δεχεται JSON book και να το αποθηκευιει. (status 200 ή 400 Bad request)

    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    @RolesAllowed({"admin"})
    public Response deleteBook(@PathParam("id") Integer id){
        Optional<BookAuthorDTO> opt = bookRepo.deleteBook(id);

        if (!opt.isPresent()){
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok(opt.get()).build();

    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    @RolesAllowed({"admin"})
    public Response updateBook(BookAuthorDTO bookAuthorDTO){

        BookAuthorDTO book1 = bookRepo.updateBook(bookAuthorDTO);

        if (book1 == null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok(book1).build();

    }

    @GET
    @Path("/{id}/cover")
    @Produces("image/jpeg")
    public Response getBookCover(@PathParam("id") Integer id) {
        Book book = bookRepo.findById(id);
        if (book == null || book.getCoverImage() == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(book.getCoverImage()).type(book.getImageContentType()).build();
    }


    //get /book/{id}
    //put id in json
    //new class bookrepository



}


