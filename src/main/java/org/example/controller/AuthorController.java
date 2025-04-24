package org.example.controller;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.example.entity.Author;
import org.example.entity.Book;
import org.example.repository.AuthorRepository;

import java.util.List;
import java.util.Optional;
@Path("author")
public class AuthorController {


    @Inject
    AuthorRepository authorRepo; // auto-injected by the container


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Author> authors(){

        return authorRepo.getAuthors();

    }

    @GET
    @Path("/findByBook/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response seeAuthorWithBookId(@PathParam("id") Integer id){

        Author author = authorRepo.findAuthorByBookId(id);

        if (author == null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok(author).build();

    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response seeAuthor(@PathParam("id") Integer id){

        Author author = authorRepo.getAuthorById(id);

        if (author == null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok(author).build();

    }


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response saveAuthor(Author author){

        author = authorRepo.saveAuthor((author));
        return Response.ok(author).build();

    }


        // POST /book να δεχεται JSON book και να το αποθηκευιει. (status 200 ή 400 Bad request)

    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response deleteAuthor(@PathParam("id") Integer id){
        Optional<Author> opt = authorRepo.deleteAuthor(id);

        if (!opt.isPresent()){
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok(opt.get()).build();

    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response updateAuthor(Author author){

        Author author1 = authorRepo.updateAuthor(author);

        if (author1 == null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok(author).build();

    }


        //get /book/{id}
        //put id in json
        //new class bookrepository



}
