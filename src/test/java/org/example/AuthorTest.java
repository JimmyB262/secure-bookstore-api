package org.example;

import jakarta.ws.rs.core.GenericType;
import org.example.controller.AuthorController;
import org.example.entity.Author;
import org.jboss.arquillian.container.test.api.Deployment;

import org.jboss.arquillian.junit.Arquillian;

import org.jboss.shrinkwrap.api.ShrinkWrap;

import org.jboss.shrinkwrap.api.spec.JavaArchive;

import org.junit.Test;

import jakarta.persistence.EntityManager;

import jakarta.persistence.PersistenceContext;

import jakarta.ws.rs.client.Client;

import jakarta.ws.rs.client.ClientBuilder;

import jakarta.ws.rs.client.Entity;

import jakarta.ws.rs.core.MediaType;

import jakarta.ws.rs.core.Response;
import org.junit.runner.RunWith;

import java.util.List;

import static org.junit.Assert.assertEquals;

import static org.junit.Assert.assertNotNull;

import static org.junit.Assert.*;


@RunWith(Arquillian.class)

public class AuthorTest {

    @PersistenceContext(unitName = "testPU")

    private EntityManager entityManager;

    @Deployment

    public static JavaArchive createDeployment() {

        return ShrinkWrap.create(JavaArchive.class)

                .addClasses(Author.class, AuthorController.class) // Add your entity and resource classes

                .addAsManifestResource("META-INF/persistence.xml"); // Add persistence.xml

    }

    @Test

    public void testGetAuthors() {

        Client client = ClientBuilder.newClient();

        Author author = new Author();

        author.setFull_name("John Doe");
        author.setAuthor_id(1);
        author.setPhone("923929");
        author.setGender('M');
        author.setAge(27);
        author.setEmail("asdsad@gmail.com");

        // Create a new user

        Response response = client.target("http://localhost:8080/Helloworld-1.0-SNAPSHOT/api/author")

                .request(MediaType.APPLICATION_JSON)

                .post(Entity.entity(author, MediaType.APPLICATION_JSON));

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());


        Response getResponse = client.target("http://localhost:8080/Helloworld-1.0-SNAPSHOT/api/author")
                .request(MediaType.APPLICATION_JSON)
                .get();

        assertEquals(Response.Status.OK.getStatusCode(), getResponse.getStatus());

        List<Author> authors = getResponse.readEntity(new GenericType<List<Author>>() {});

        assertFalse(authors.isEmpty());

        boolean containsJohn = authors.stream().anyMatch(a -> "John Doe".equals(a.getFull_name()));
        assertTrue(containsJohn);

        // Optionally, you can retrieve the user to verify it was created

/*        Long userId = response.readEntity(User.class).getId();

        User foundUser = client.target("http://localhost:8080/your-app-context/users/" + userId)

                .request(MediaType.APPLICATION_JSON)

                .get(User.class);

        assertNotNull(foundUser);

        assertEquals("John Doe", foundUser.getName());*/

    }

}

 
