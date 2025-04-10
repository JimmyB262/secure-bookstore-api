package org.example;


import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceUnit;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.List;

@Path("/book")
public class Controler {


    @PersistenceUnit(unitName = "myUnit")
    EntityManager entityManager;



    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Book> books(){
        List<Book> results = entityManager
                .createQuery("Select b from Book b", Book.class)
                .getResultList();
        return results;
    }


    // GET /book/{id}   για να διαβαστεί ένα βιβλίο:   να επιστρέφει JSON (status 200: ή 404 για notfound)


    // GET /book  JSON list με όλα τα βιβλία

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response saveBook(Book book){
        book.setId(0);
        entityManager.persist(book);

            return Response.ok(book).build();

    }

    // POST /book να δεχεται JSON book και να το αποθηκευιει. (status 200 ή 400 Bad request)






}


