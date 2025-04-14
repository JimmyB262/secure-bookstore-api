package org.example;


import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.Optional;
import java.util.List;
@Path("/book")
@RequestScoped
public class Controler {


    @Inject
    BookRepository bookRepo; // auto-injected by the container


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Book> books(){

        return bookRepo.getBooks();

    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response seeBook(@PathParam("id") Integer id){

        Book book = bookRepo.getBookById(id);

        if (book == null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok(book).build();

    }

    // GET /book/{id}   για να διαβαστεί ένα βιβλίο:   να επιστρέφει JSON (status 200: ή 404 για not found)


    // GET /book  JSON list με όλα τα βιβλία

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response saveBook(Book book){

        book = bookRepo.saveBook((book));
        return Response.ok(book).build();

    }


    // POST /book να δεχεται JSON book και να το αποθηκευιει. (status 200 ή 400 Bad request)

    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response deleteBook(@PathParam("id") Integer id){
        Optional<Book> opt = bookRepo.deleteBook(id);

        if (!opt.isPresent()){
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok(opt.get()).build();

    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response updateBook(Book book){

        Book book1 = bookRepo.updateBook(book);

        if (book1 == null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok(book).build();

    }


    //get /book/{id}
    //put id in json
    //new class bookrepository



}


